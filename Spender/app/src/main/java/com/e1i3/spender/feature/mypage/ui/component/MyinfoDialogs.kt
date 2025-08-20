package com.e1i3.spender.feature.mypage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.e1i3.spender.feature.mypage.domain.model.User
import com.e1i3.spender.ui.theme.DefaultFontColor
import com.e1i3.spender.ui.theme.LightPointColor
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun EditNicknameDialog(
    title: String,
    user: User?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean = false
) {
    var nickName by remember { mutableStateOf(user?.displayNickname ?: "") }

    Dialog(onDismissRequest = { if (!isLoading) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 28.dp,
                    end = 24.dp,
                    bottom = 14.dp
                ),
            ) {
                Text(
                    text = title,
                    style = Typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = nickName,
                        onValueChange = { if (!isLoading) nickName = it },
                        placeholder = {
                            Text(
                                "닉네임을 입력해주세요",
                                color = MaterialTheme.colorScheme.onTertiary,
                                fontSize = 14.sp
                            )
                        },
                        singleLine = true,
                        enabled = !isLoading,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                            focusedIndicatorColor = PointColor,
                            disabledContainerColor = Color.Transparent,
                            disabledIndicatorColor = MaterialTheme.colorScheme.tertiary
                        ),
                        textStyle = Typography.titleMedium
                    )
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightPointColor,
                            disabledContainerColor = LightPointColor.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "취소",
                            style = Typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isLoading) DefaultFontColor.copy(alpha = 0.5f) else DefaultFontColor
                        )
                    }
                    Button(
                        onClick = { if (nickName.isNotBlank() && !isLoading) onConfirm(nickName) },
                        modifier = Modifier.weight(1f),
                        enabled = nickName.isNotBlank() && !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PointColor,
                            disabledContainerColor = PointColor.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "저장",
                                color = Color.White,
                                style = Typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EditImageDialog(
    title: String,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 28.dp,
                    end = 24.dp,
                    bottom = 14.dp
                ),
            ) {
                Text(
                    text = title,
                    style = Typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onCameraClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "카메라로 촬영",
                            style = Typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = DefaultFontColor
                        )
                    }
                    Button(
                        onClick = onGalleryClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "갤러리에서 선택",
                            color = Color.White,
                            style = Typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}