package com.e1i3.spender.feature.mypage.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e1i3.spender.R
import com.e1i3.spender.feature.mypage.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor() : ViewModel() {

    private val _user = MutableStateFlow(User(displayNickname = "사용자"))
    val user = _user.asStateFlow()

    private val _updateNicknameState =
        MutableStateFlow<UpdateNicknameState>(UpdateNicknameState.Idle)
    val updateNicknameState = _updateNicknameState.asStateFlow()

    private val _updateProfileImageState =
        MutableStateFlow<UpdateProfileImageState>(UpdateProfileImageState.Idle)
    val updateProfileImageState = _updateProfileImageState.asStateFlow()

    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            return
        }

        FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val provider = document.getString("provider")
                    val name = document.getString("name")
                    val email = document.getString("email")
                    val nickname = document.getString("nickname")

                    val iconRes = when (provider) {
                        "google" -> R.drawable.google_icon
                        "naver" -> R.drawable.naverlogo_icon
                        "kakao" -> R.drawable.kakao_icon
                        else -> null
                    }

                    val displayName = when (provider) {
                        "google" -> email ?: "사용자"
                        else -> name ?: "사용자"
                    }

                    val displayEmail = when (provider) {
                        "kakao" -> email ?: "사용자"
                        else -> email ?: "사용자"
                    }

                    val displayNickname = when (provider) {
                        "kakao" -> nickname ?: "사용자"
                        else -> nickname ?: "사용자"
                    }

                    val profileUrl = document.getString("photoUrl")

                    _user.value = User(
                        displayName = displayName,
                        displayEmail = displayEmail,
                        providerIcon = iconRes,
                        displayNickname = displayNickname,
                        profileUrl = profileUrl
                    )
                }
            }
            .addOnFailureListener {
                val displayName = firebaseUser.displayName ?: firebaseUser.email ?: "사용자"
                _user.value = User(
                    displayName = displayName,
                    displayNickname = displayName
                )
            }

    }

    fun updateNickname(newNickname: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            _updateNicknameState.value = UpdateNicknameState.Error("로그인이 필요합니다.")
            resetUpdateNicknameState()
            return
        }

        if (newNickname.isBlank()) {
            _updateNicknameState.value = UpdateNicknameState.Error("닉네임을 입력해주세요.")
            resetUpdateNicknameState()
            return
        }

        if (newNickname.length > 20) {
            _updateNicknameState.value = UpdateNicknameState.Error("닉네임은 20자 이하로 입력해주세요.")
            resetUpdateNicknameState()
            return
        }

        if (newNickname == _user.value.displayNickname) {
            _updateNicknameState.value = UpdateNicknameState.Error("현재 닉네임과 동일합니다.")
            resetUpdateNicknameState()
            return
        }

        _updateNicknameState.value = UpdateNicknameState.Loading

        val userDocRef =
            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)

        userDocRef.update("nickname", newNickname)
            .addOnSuccessListener {
                _user.value = _user.value.copy(displayNickname = newNickname)
                _updateNicknameState.value = UpdateNicknameState.Success
                resetUpdateNicknameState()
            }
            .addOnFailureListener { exception ->
                _updateNicknameState.value = UpdateNicknameState.Error(
                    exception.message ?: "닉네임 업데이트에 실패했습니다."
                )
                resetUpdateNicknameState()
            }
    }

    private fun resetUpdateNicknameState() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            _updateNicknameState.value = UpdateNicknameState.Idle
        }
    }

    fun updateProfileImage(context: Context, imageUri: Uri) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            _updateProfileImageState.value = UpdateProfileImageState.Error("로그인이 필요합니다.")
            resetUpdateProfileImageState()
            return
        }

        _updateProfileImageState.value = UpdateProfileImageState.Loading

        viewModelScope.launch {
            try {
                val imageFile = when {
                    imageUri.scheme == "content" -> {
                        val inputStream = context.contentResolver.openInputStream(imageUri)
                        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
                        inputStream?.use { input ->
                            tempFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        tempFile
                    }

                    imageUri.scheme == "file" -> File(imageUri.path!!)
                    else -> throw IllegalArgumentException("지원하지 않는 유형입니다.")
                }

                val compressedImageFile = Compressor.compress(context, imageFile)

                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("users/${firebaseUser.uid}/images/profile.jpg")

                val uploadTask = imageRef.putFile(Uri.fromFile(compressedImageFile))
                val downloadUrl = uploadTask.await().storage.downloadUrl.await()

                val userDocRef =
                    FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)
                userDocRef.update("photoUrl", downloadUrl.toString()).await()

                _user.value = _user.value.copy(profileUrl = downloadUrl.toString())
                _updateProfileImageState.value = UpdateProfileImageState.Success
                resetUpdateProfileImageState()

            } catch (e: Exception) {
                _updateProfileImageState.value = UpdateProfileImageState.Error(
                    e.message ?: "프로필 이미지 업데이트에 실패했습니다."
                )
                resetUpdateProfileImageState()
            }
        }
    }

    fun deleteProfileImage() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            _updateProfileImageState.value = UpdateProfileImageState.Error("로그인이 필요합니다.")
            resetUpdateProfileImageState()
            return
        }

        _updateProfileImageState.value = UpdateProfileImageState.Loading

        viewModelScope.launch {
            try {
                val userDocRef =
                    FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)
                userDocRef.update("photoUrl", null).await()

                _user.value = _user.value.copy(profileUrl = null)
                _updateProfileImageState.value = UpdateProfileImageState.Success
                resetUpdateProfileImageState()

            } catch (e: Exception) {
                _updateProfileImageState.value = UpdateProfileImageState.Error(
                    e.message ?: "프로필 이미지 삭제에 실패했습니다."
                )
                resetUpdateProfileImageState()
            }
        }
    }

    private fun resetUpdateProfileImageState() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            _updateProfileImageState.value = UpdateProfileImageState.Idle
        }
    }

    sealed class UpdateNicknameState {
        object Idle : UpdateNicknameState()
        object Loading : UpdateNicknameState()
        object Success : UpdateNicknameState()
        data class Error(val message: String) : UpdateNicknameState()
    }

    sealed class UpdateProfileImageState {
        object Idle : UpdateProfileImageState()
        object Loading : UpdateProfileImageState()
        object Success : UpdateProfileImageState()
        data class Error(val message: String) : UpdateProfileImageState()
    }

}