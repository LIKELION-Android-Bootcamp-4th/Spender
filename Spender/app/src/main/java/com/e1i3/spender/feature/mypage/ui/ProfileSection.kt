package com.e1i3.spender.feature.mypage.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.e1i3.spender.R
import com.e1i3.spender.feature.mypage.ui.component.CircularImage
import com.e1i3.spender.feature.mypage.ui.component.EditButton
import com.e1i3.spender.feature.mypage.ui.component.EditNicknameDialog
import com.e1i3.spender.feature.mypage.ui.viewmodel.MypageViewModel
import com.e1i3.spender.ui.theme.Typography

@Composable
fun ProfileSection(nickName: String) {
    var showEditNickNameAlert by remember { mutableStateOf(false) }
    val viewModel: MypageViewModel = hiltViewModel()
    val context = LocalContext.current

    val user by viewModel.user.collectAsState()
    val updateNicknameState by viewModel.updateNicknameState.collectAsState()

    val currentNickname = when {
        user.displayNickname.isNotBlank() && user.displayNickname != "사용자" -> user.displayNickname
        nickName.isNotBlank() -> nickName
        else -> "사용자"
    }

    LaunchedEffect(updateNicknameState) {
        when (updateNicknameState) {
            is MypageViewModel.UpdateNicknameState.Success -> {
                Toast.makeText(context, "닉네임이 변경되었어요!", Toast.LENGTH_SHORT).show()
                showEditNickNameAlert = false
            }

            is MypageViewModel.UpdateNicknameState.Error -> {
                val errorMessage =
                    (updateNicknameState as MypageViewModel.UpdateNicknameState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            is MypageViewModel.UpdateNicknameState.Loading -> {}
            else -> {}
        }
    }

    Column(
        modifier = Modifier
    ) {
        Text(
            text = "프로필",
            color = MaterialTheme.colorScheme.onBackground,
            style = Typography.titleSmall,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.warning_icon),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "다른 사람들에게 보이는 정보예요!",
                style = Typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularImage(profileUrl = null)

            Spacer(Modifier.height(16.dp))

            Row {
                EditButton(text = "변경", onClick = {}) // TODO: 카메라/갤러리 다이얼로그
                Spacer(Modifier.width(8.dp))
                EditButton(text = "삭제", onClick = {}) // TODO: 기존의 profileUrl을 삭제하고 기본 아이콘으로 변경
            }
        }

        Spacer(Modifier.height(64.dp))

        Row {
            Text(
                text = "닉네임",
                style = Typography.titleMedium
            )
            Spacer(Modifier.width(16.dp))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = currentNickname,
                        style = Typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { showEditNickNameAlert = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "닉네임 수정",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary,
                    thickness = 1.dp,
                )
            }
        }
    }

    if (showEditNickNameAlert) {
        EditNicknameDialog(
            title = "닉네임 변경",
            user = user,
            onDismiss = {
                if (updateNicknameState !is MypageViewModel.UpdateNicknameState.Loading) {
                    showEditNickNameAlert = false
                }
            },
            onConfirm = { newNickname ->
                viewModel.updateNickname(newNickname)
            },
            isLoading = updateNicknameState is MypageViewModel.UpdateNicknameState.Loading
        )
    }
}
