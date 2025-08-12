package com.example.spender.feature.expense.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.spender.BuildConfig
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import android.Manifest
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.SideEffect
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrContent(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel
) {
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            cameraImageUri?.let {
                val imageBytes = context.contentResolver.openInputStream(it)?.readBytes()
                imageBytes?.let { viewModel.analyzeReceipt(it, "jpg") }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileExtension = context.contentResolver.getType(it)?.let { mimeType ->
                MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            } ?: "jpg"

            val imageByteArray = context.contentResolver.openInputStream(it)?.readBytes()
            imageByteArray?.let { bytes ->
                viewModel.analyzeReceipt(bytes, fileExtension)
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = createTempImageUri(context)
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // 영수증 인식하기 카드
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1.5f)
                .clickable(enabled = !uiState.isLoading) {
                    viewModel.onOcrDialogVisibilityChange(true)
                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "영수증 인식하기",
                    style = Typography.titleMedium
                )
                Spacer(Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "영수증 인식",
                    modifier = Modifier.size(80.dp),
                    tint = PointColor
                )
            }
        }
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = PointColor,
                strokeWidth = 5.dp
            )
        }
    }
    if (uiState.isOcrDialogVisible) {
        OcrSelectionDialog(
            onDismissRequest = { viewModel.onOcrDialogVisibilityChange(false) },
            onTakePhotoClick = {
                val uri = createTempImageUri(context)
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                viewModel.onOcrDialogVisibilityChange(false)
            },
            onSelectImageClick = {
                galleryLauncher.launch("image/*")
                viewModel.onOcrDialogVisibilityChange(false)
            }
        )
    }

}

@Composable
private fun OcrSelectionDialog(
    onDismissRequest: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onSelectImageClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("영수증 등록") },
        text = { Text("등록할 방식을 선택해주세요.") },
        confirmButton = {
            TextButton(onClick = onTakePhotoClick) {
                Text("카메라로 촬영")
            }
        },
        dismissButton = {
            TextButton(onClick = onSelectImageClick) {
                Text("갤러리에서 선택")
            }
        }
    )
}

private fun createTempImageUri(context: Context): Uri {
    val tempFile = File(context.cacheDir, "temp_receipt.jpg")
    return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", tempFile)
}

//@Preview(showBackground = true)
//@Composable
//fun OcrRegistrationScreenPreview() {
//    SpenderTheme {
//        OcrRegistrationScreen()
//    }
//}
