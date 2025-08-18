package com.example.spender.feature.expense.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.feature.expense.data.remote.ExpenseDto
import com.example.spender.feature.expense.data.remote.RegularExpenseDto
import com.example.spender.feature.expense.data.repository.ExpenseRepository
import com.example.spender.feature.expense.data.repository.RegularExpenseRepository
import com.example.spender.feature.expense.domain.model.Emotion
import com.example.spender.feature.mypage.data.repository.CategoryRepository
import com.example.spender.feature.mypage.domain.model.Category
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Base64
import android.util.Log
import java.util.*
import com.example.spender.BuildConfig
import com.example.spender.core.data.service.OcrApiService
import com.example.spender.feature.expense.data.remote.OcrImageRequest
import com.example.spender.feature.expense.data.remote.OcrRequest
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date

sealed class RegistrationEvent {
    data class ShowToast(val message: String) : RegistrationEvent()
    data object NavigateBack : RegistrationEvent()
    data class OcrSuccess(val title: String, val amount: String, val date: String) : RegistrationEvent()
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val regularExpenseRepository: RegularExpenseRepository,
    private val ocrApiService: OcrApiService,
): ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState = _uiState.asStateFlow()

    private val _expenseCategories = MutableStateFlow<List<Category>>(emptyList())
    val expenseCategories = _expenseCategories.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RegistrationEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val emotionsList = listOf(
        Emotion(id = "satisfied", name = "만족"),
        Emotion(id = "dissatisfied", name = "불만"),
        Emotion(id = "impulsive", name = "충동"),
        Emotion(id = "unfair", name = "억울")
    )

    init {
        _uiState.update {
            it.copy(
                emotions = emotionsList,
                selectedEmotion = emotionsList.first()
            )
        }
        loadExpenseCategories()
    }

    private fun loadExpenseCategories() {
        val userId = getFirebaseAuth() ?: return
        categoryRepository.getCategories(userId, "EXPENSE") { categoryList ->
            _expenseCategories.value = categoryList
        }
    }

    //이벤트 핸들러
    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(category = category.name, categoryId = category.id) }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun onAmountChange(newAmount: String) {
        if (newAmount.length <= 10) {
            _uiState.update { it.copy(amount = newAmount.filter { char -> char.isDigit() }) }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onMemoChange(newMemo: String) {
        _uiState.update { it.copy(memo = newMemo) }
    }

    fun onEmotionSelected(emotion: Emotion) {
        _uiState.update { it.copy(selectedEmotion = emotion) }
    }

    fun onOcrDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isOcrDialogVisible = isVisible) }
    }
    fun onRepeatDaySelected(day: Int) {
        _uiState.update { it.copy(dayOfMonth = day) }
    }
    fun onRepeatSheetVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isRepeatSheetVisible = isVisible) }
    }
    fun onRegisterClick() {
        viewModelScope.launch {
            when (_uiState.value.selectedTabIndex) {
                1 -> registerExpense()
                2 -> registerRecurringExpense()
            }
        }
    }
    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }
    //지출등록
    private suspend fun registerExpense() {
        _uiState.update { it.copy(isUploading = true) }
        val userId = getFirebaseAuth() ?: return
        val currentState = _uiState.value
        var imageUrl: String? = null

        if (currentState.amount.isBlank() || currentState.categoryId.isBlank()) {
            _eventFlow.emit(RegistrationEvent.ShowToast("금액과 카테고리는 필수 항목입니다."))
            return
        }

        if (currentState.selectedImageUri != null) {
            imageUrl = uploadImageToStorage(userId, currentState.selectedImageUri)
            if (imageUrl == null) {
                _eventFlow.emit(RegistrationEvent.ShowToast("이미지 업로드에 실패했습니다."))
                _uiState.update { it.copy(isUploading = false) }
                return
            }
        }

        val expenseDto = ExpenseDto(
            amount = currentState.amount.toLongOrNull() ?: 0L,
            title = currentState.title,
            memo = currentState.memo,
            date = Timestamp(currentState.date),
            categoryId = currentState.categoryId,
            emotion = currentState.selectedEmotion?.id ?: emotionsList.first().id,
            imageUrl = imageUrl ?: ""
        )

        val isSuccess = expenseRepository.addExpense(userId, expenseDto)
        if (isSuccess) {
            _eventFlow.emit(RegistrationEvent.ShowToast("저장되었습니다"))
            clearInputs()
            _eventFlow.emit(RegistrationEvent.NavigateBack)
        } else {
            _eventFlow.emit(RegistrationEvent.ShowToast("저장에 실패했습니다."))
        }
        _uiState.update { it.copy(isUploading = false) }
    }

    private suspend fun registerRecurringExpense() {
        val userId = getFirebaseAuth() ?: return
        val currentState = _uiState.value

        if (currentState.amount.isBlank() || currentState.title.isBlank() || currentState.categoryId.isBlank()) {
            _eventFlow.emit(RegistrationEvent.ShowToast("금액, 내용, 카테고리는 필수 항목입니다."))
            return
        }

        val regularExpenseDto = RegularExpenseDto(
            amount = currentState.amount.toLongOrNull() ?: 0L,
            title = currentState.title.ifBlank { currentState.category },
            memo = currentState.memo,
            categoryId = currentState.categoryId,
            first_payment_date = Timestamp(currentState.date),
            repeat_cycle = "MONTHLY",
            day = currentState.dayOfMonth
        )

        if (regularExpenseRepository.addRegularExpense(userId, regularExpenseDto)) {
            _eventFlow.emit(RegistrationEvent.ShowToast("정기 지출이 등록되었습니다"))
            clearInputs()
            _eventFlow.emit(RegistrationEvent.NavigateBack)
        } else {
            _eventFlow.emit(RegistrationEvent.ShowToast("저장에 실패했습니다."))
        }
    }

    private suspend fun uploadImageToStorage(userId: String, uri: Uri): String? {
        return try {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("expenses/${userId}/${System.currentTimeMillis()}.jpg")

            imageRef.putFile(uri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }


    // 지출등록 후 필드 초기화
    private fun clearInputs() {
        _uiState.update {
            it.copy(
                title = "",
                amount = "",
                memo = "",
                category = "카테고리 선택",
                categoryId = "",
                selectedEmotion = emotionsList.first(),
                selectedImageUri = null
            )
        }
    }

    fun analyzeReceipt(imageByteArray: ByteArray, format: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val base64Image = Base64.encodeToString(imageByteArray, Base64.NO_WRAP)
                val request = OcrRequest(
                    requestId = UUID.randomUUID().toString(),
                    images = listOf(OcrImageRequest(format = format, data = base64Image))
                )

                val response = ocrApiService.analyzeReceipt(
                    url = BuildConfig.NAVER_OCR_API_URL,
                    secretKey = BuildConfig.NAVER_OCR_CLIENT_SECRET,
                    request = request
                )

                val result = response.images.firstOrNull()?.receipt?.result
                Log.d("OCR_RESULT_RAW", "Store: ${result?.storeInfo?.name?.text}, Price: ${result?.totalPrice?.price?.text}, Date: ${result?.paymentInfo?.date?.text}")

                if (response.images.firstOrNull()?.inferResult == "SUCCESS") {
                    val title = result?.storeInfo?.name?.text ?: "인식 실패"

                    val amountString = result?.totalPrice?.price?.text ?: "0"
                    val amount = amountString.replace(Regex("[^0-9]"), "")

                    val dateString = result?.paymentInfo?.date?.text ?: ""
                    val parsedDate = parseOcrDate(dateString)
                    val formattedDateString = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(parsedDate)

                    _eventFlow.emit(RegistrationEvent.ShowToast("인식 완료!!"))

                    Log.d("OCR_RESULT_PARSED", "Title: $title, Amount: $amount, Date: $formattedDateString")

                    _eventFlow.emit(RegistrationEvent.OcrSuccess(title, amount, formattedDateString))
                } else {
                    _eventFlow.emit(RegistrationEvent.ShowToast("영수증 인식에 실패했습니다."))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("OCR_API_ERROR", "HTTP Error: ${e.code()}, Body: $errorBody")
                _eventFlow.emit(RegistrationEvent.ShowToast("오류 발생: ${e.code()} - 서버 응답을 확인하세요."))
            } catch (e: Exception) {
                Log.e("OCR_API_ERROR", "OCR 분석 중 오류 발생", e)
                _eventFlow.emit(RegistrationEvent.ShowToast("오류가 발생했습니다: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onDateSelected(timestamp: Long?) {
        timestamp ?: return
        _uiState.update { it.copy(date = Date(timestamp)) }
    }

    fun onDateDialogVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(isDatePickerDialogVisible = isVisible) }
    }

    fun setInitialTabIndex(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}

private fun parseOcrDate(dateString: String): Date {
    // OCR이 인식할 수 있는 여러 날짜 형식을 시도
    val formats = listOf(
        "yyyy-MM-dd",
        "yyyy.MM.dd",
        "yyyy/MM/dd"
    )
    for (format in formats) {
        try {
            val cleanedDateString = dateString.replace(Regex("[^0-9./-]"), "")
            return SimpleDateFormat(format, Locale.KOREA).parse(cleanedDateString) ?: Date()
        } catch (e: Exception) {

        }
    }
    return Date()
}