package com.example.spender.feature.onboarding

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spender.feature.onboarding.ui.OnboardingViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.core.ui.CustomLongButton
import com.example.spender.feature.onboarding.data.OnboardingPref
import com.example.spender.feature.onboarding.ui.BudgetInputField
import com.example.spender.feature.onboarding.ui.FirstPage
import com.example.spender.feature.onboarding.ui.PageIndicator
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val onComplete: (Boolean) -> Unit = { isGranted ->
        Log.d("푸시 알림 설정 동의", "결과: $isGranted")
        saveDefaultNotificationSettingsToFirestore(isGranted)

        viewModel.saveBudget { success ->
            if (success) { // TODO : 알림 설정을 하고 나서 예산 뜨니까 이상함 -> 토스트 뺄까요...?
                Toast.makeText(context, "예산이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                Log.d("Budget", "저장 성공")
            } else {
                Log.d("Budget", "저장 실패")
            }
        }

        OnboardingPref.setShown(context)
        navController.navigate(Screen.MainScreen.route) {
            popUpTo(Screen.OnboardingScreen.route) { inclusive = true }
        }
    }

    val currentPage by viewModel.currentPage.collectAsState()
    val budget = viewModel.budget

    val titles = context.resources.getStringArray(R.array.onboarding_title).toList()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onComplete(isGranted)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageIndicator(currentPage = currentPage, pageCount = titles.size)

            Text(
                text = titles[currentPage],
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (currentPage == 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "똑똑한 소비 습관을 만들기 위해 \n지출이는 아래와 같이 도와줄 거예요!",
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(88.dp))
                FirstPage()
            }

            if (currentPage == 1) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "지출이가 예산 관리도 도와드릴게요!",
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(88.dp))
                BudgetInputField(
                    budget = budget,
                    onBudgetChange = { viewModel.updateBudget(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        CustomLongButton(
            text = if (currentPage < 2) "다음" else "시작하기",
            onClick = {
                if (currentPage < 2) {
                    viewModel.onNext()
                } else { // TODO: 콜백 처리?!?!
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val granted = androidx.core.content.ContextCompat.checkSelfPermission(
                            context, Manifest.permission.POST_NOTIFICATIONS
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                        if (granted) {
                            onComplete(true)
                        } else {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        onComplete(true)
                    }
                }
            },
            isEnabled = currentPage != 1 || budget > 0
        )
    }
}

fun saveDefaultNotificationSettingsToFirestore(enabled: Boolean) {
    val uid = Firebase.auth.currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    val settingsMap = mapOf(
        "notificationSettings" to mapOf(
            "budgetAlert" to enabled,
            "reportAlert" to enabled,
            "reminderAlert" to enabled
        )
    )

    db.collection("users")
        .document(uid)
        .set(settingsMap, SetOptions.merge())
        .addOnSuccessListener {
            Log.d("Firestore", "알림 설정 초기화 완료 (enabled=$enabled)")
        }
        .addOnFailureListener{
            Log.d("Firestore", "알림 설정 초기화 실패", it)
        }
}
