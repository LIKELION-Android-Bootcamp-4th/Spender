package com.example.spender.feature.mypage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.spender.feature.mypage.domain.model.Category
import com.example.spender.ui.theme.DefaultFontColor
import com.example.spender.ui.theme.LightPointColor
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

// 추가/수정 다이얼로그
@Composable
fun CategoryEditDialog(
    isEditing: Boolean,
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(if (isEditing) category?.name ?: "" else "") }

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
                    text = if (isEditing) "카테고리 수정" else "카테고리 추가",
                    style = Typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            Text(
                                "카테고리 이름을 입력해주세요",
                                color = MaterialTheme.colorScheme.onTertiary,
                                fontSize = 14.sp
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                            focusedIndicatorColor = PointColor
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
                        colors = ButtonDefaults.buttonColors(containerColor = LightPointColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "취소",
                            style = Typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = DefaultFontColor
                        )
                    }
                    Button(
                        onClick = { if (text.isNotBlank()) onConfirm(text) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            if (isEditing) "수정" else "저장",
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

// 삭제 다이얼로그
@Composable
fun CategoryDeleteDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (category == null) return

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
                    text = "카테고리 삭제",
                    style = Typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (category.color != null) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(category.color)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(text = category.name, style = Typography.titleMedium)
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = LightPointColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("취소", style = Typography.bodySmall, fontWeight = FontWeight.Bold, color = DefaultFontColor)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "삭제",
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