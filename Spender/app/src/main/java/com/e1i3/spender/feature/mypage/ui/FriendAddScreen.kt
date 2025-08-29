package com.e1i3.spender.feature.mypage.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.mypage.ui.viewmodel.FriendViewModel
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography

@Composable
fun FriendAddScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val viewModel: FriendViewModel = hiltViewModel()
    val myCode by viewModel.myCode.collectAsState()
    val yourCode by viewModel.yourCode.collectAsState()
    val expiredAt by viewModel.expiredAt.collectAsState()

    val clipboard = context.getSystemService(ClipboardManager::class.java)
    fun copy() {
        if (myCode.isNotEmpty()) {
            clipboard.setPrimaryClip(ClipData.newPlainText(myCode, myCode))
            Toast.makeText(context, "코드가 복사되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    fun share() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "친구가 꽉잡아 지출이로 초대했어요!\n\nhttps://spender-5f3c4.web.app/invite/$myCode")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, "공유하기")
        context.startActivity(shareIntent)
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                title = "친구 추가",
                navHostController,
                showBackButton = true,
            )
        },
        content = { padding ->
            Column (modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
                Row {
                    Text("내 초대코드", style = Typography.titleMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "코드 공유",
                        modifier = Modifier.clickable { share() }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "코드 새로고침",
                        modifier = Modifier.clickable { viewModel.refresh() }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(106.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { copy() }
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(myCode, style = Typography.bodyMedium)
                        Spacer(Modifier.height(5.dp))
                        Row {
                            Icon(imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = "복사",
                                modifier = Modifier.size(16.dp).align(Alignment.Bottom),
                                tint = MaterialTheme.colorScheme.onTertiary,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("초대코드 복사하기", style = Typography.bodySmall, color = MaterialTheme.colorScheme.onTertiary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("초대코드 만료일자: $expiredAt", style = Typography.bodyMedium, color = MaterialTheme.colorScheme.onTertiary)
                Spacer(modifier = Modifier.height(30.dp))
                Text("초대코드로 친구 추가하기", style = Typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = yourCode,
                    onValueChange = { viewModel.onInput(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "초대코드를 입력하세요",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    },
                    singleLine = true,
                    textStyle = Typography.titleMedium,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary
                    )
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        viewModel.addFriend { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            if (msg == "친구 추가에 성공했습니다.") viewModel.clear()
                            navHostController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        "친구 등록",
                        modifier = Modifier.padding(vertical = 6.dp),
                        style = Typography.titleSmall
                    )
                }
            }
        }
    )
}