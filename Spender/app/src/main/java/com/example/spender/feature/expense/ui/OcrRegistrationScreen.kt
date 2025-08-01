package com.example.spender.feature.expense.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.SpenderTheme
import com.example.spender.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrRegistrationScreen() {

    // 다이얼로그의 표시 상태를 관리하는 변수
    var showDialog by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("영수증", "지출", "정기지출")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("지출 등록", style = Typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { /* 뒤로가기 */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 탭 메뉴
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(10.dp)),
                containerColor = Color(0xFFF0F2F5),
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .fillMaxSize()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .height(48.dp)
                            .zIndex(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { selectedTabIndex = index }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            color = if (isSelected) Color.Black else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontFamily = NotoSansFamily,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // '영수증 인식하기' 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1.5f)
                    .clickable { showDialog = true },
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
        if (showDialog) {
            OcrSelectionDialog(
                onDismissRequest = { showDialog = false },
                onTakePhotoClick = {
                    // TODO: 카메라 촬영 로직 실행
                    showDialog = false
                },
                onSelectImageClick = {
                    // TODO: 갤러리 이미지 선택 로직 실행
                    showDialog = false
                }
            )
        }
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
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("취소")
            }
        },
        /*
        content = {
            Column {
                DialogButton(text = "카메라로 촬영하기", icon = Icons.Default.CameraAlt, onClick = onTakePhotoClick)
                HorizontalDivider()
                DialogButton(text = "갤러리에서 선택", icon = Icons.Default.Image, onClick = onSelectImageClick)
            }
        }
        */
    )
}

@Preview(showBackground = true)
@Composable
fun OcrRegistrationScreenPreview() {
    SpenderTheme {
        OcrRegistrationScreen()
    }
}
