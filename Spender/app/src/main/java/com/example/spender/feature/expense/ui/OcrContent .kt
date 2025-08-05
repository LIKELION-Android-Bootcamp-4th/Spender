package com.example.spender.feature.expense.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrContent(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel
) {
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
                .clickable { viewModel.onOcrDialogVisibilityChange(true) },
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
    }
    if (uiState.isOcrDialogVisible) {
        OcrSelectionDialog(
            onDismissRequest = { viewModel.onOcrDialogVisibilityChange(false) },
            onTakePhotoClick = {
                viewModel.onOcrDialogVisibilityChange(false)
                // TODO: 카메라 촬영 로직
            },
            onSelectImageClick = {
                viewModel.onOcrDialogVisibilityChange(false)
                // TODO: 갤러리 선택 로직
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

//@Preview(showBackground = true)
//@Composable
//fun OcrRegistrationScreenPreview() {
//    SpenderTheme {
//        OcrRegistrationScreen()
//    }
//}
