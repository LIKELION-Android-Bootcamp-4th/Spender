package com.example.spender.feature.onboarding

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.spender.feature.onboarding.ui.PageIndicator
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val currentPage by viewModel.currentPage.collectAsState()
    val budget = viewModel.budget

    val titles = context.resources.getStringArray(R.array.onboarding_title).toList()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("푸시 알림 설정 동의", "알림 권한 허용!!!!")
            // 👉 Firestore에 기본 알림 설정 저장
            saveDefaultNotificationSettingsToFirestore(true)
        } else {
            saveDefaultNotificationSettingsToFirestore(false)
            Log.d("푸시 알림 설정 동의", "알림 권한 거부!!!!")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                textAlign = TextAlign.Center
            )

            if (currentPage == 1) {
                Spacer(modifier = Modifier.height(80.dp))
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
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        // Android 12 이하는 바로 저장
                        saveDefaultNotificationSettingsToFirestore(true)
                    }

                    OnboardingPref.setShown(context)
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.OnboardingScreen.route) {
                            inclusive = true
                        }
                    }
                }
            },
            isEnabled = currentPage != 1 || budget < 0
        )
    }
}

fun saveDefaultNotificationSettingsToFirestore(enabled: Boolean) {
    val uid = Firebase.auth.currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    val defaultSettings = mapOf(
        "budget_alert" to enabled,
        "report_alert" to enabled,
        "reminder_alert" to enabled
    )

    db.collection("users")
        .document(uid)
        .collection("notification_settings")
        .document("notification_settings")
        .set(defaultSettings)
        .addOnSuccessListener {
            Log.d("Firestore", "알림 설정 초기화 완료 : $enabled")
        }
}
