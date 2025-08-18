package com.example.spender.feature.expense.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.spender.BuildConfig
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))

        Box(
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                        painter = painterResource(id = com.example.spender.R.drawable.ic_receipt),
                        contentDescription = "영수증 인식",
                        modifier = Modifier.size(80.dp),
                        tint = PointColor
                    )
                }
            }
            if (uiState.isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = PointColor,
                        strokeWidth = 5.dp
                    )
                    Spacer(Modifier.height(120.dp))
                    Text(
                        text = "영수증 인식중...",
                        style = Typography.titleMedium,
                        color = PointColor
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "• 최대한 바르게 펴진 문서를 기울임 없이, 영역에 가득 차도록 인식해 주십시오.",
                style = Typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(Modifier.height(12.dp))

            Text(
                text = "• 접힘, 구겨짐, 빛 반사, 그늘로 인해 글자가 잘 보이지 않으면 정확한 값을 추출할 수 없습니다.",
                style = Typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary
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
