package com.example.spender.feature.auth.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.spender.ui.theme.GooglePointColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.spender.R
import com.example.spender.core.data.remote.auth.LoginType
import com.example.spender.core.data.service.getFirebaseAuth
import com.example.spender.core.data.service.login
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.WhiteColor
import com.example.spender.ui.theme.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

@Composable
fun GoogleLogin(
    navController: NavHostController
) {

    val viewModel: AuthViewModel = viewModel()
    val googleToken = stringResource(id = R.string.default_web_client_id)
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.googleLogin(result) {
            Log.d("Login", "Google SignIn Success!")
            login(FirebaseAuth.getInstance().currentUser, LoginType.GOOGLE.type)
            Log.d("Login", getFirebaseAuth() ?: "failed")
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(Screen.AuthScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        border = BorderStroke(1.dp, color = GooglePointColor),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhiteColor
        ),
        onClick = {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleToken)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "구글 계정으로 시작하기",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium.copy(
                    color = GooglePointColor
                )
            )
        }
    }
}
