package com.example.spender.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.spender.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    val user = FirebaseAuth.getInstance().currentUser
//    LaunchedEffect(Unit) {
//        delay(1000) // 시간 변경 해도 됨...d임시!!
////        if (user != null) {
////            navController.navigate("main") {
////                popUpTo("splash") { inclusive = true }
////            }
////        } else {
////            navController.navigate("auth") {
////                popUpTo("splash") { inclusive = true }
////            }
////        }
//    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.spender_default),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(250.dp),
        )
    }
}

//navController: NavHostController

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}