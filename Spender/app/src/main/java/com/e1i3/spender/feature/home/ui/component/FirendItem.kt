package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.e1i3.spender.core.ui.CustomDialog
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.navigation.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendItem(navHostController: NavHostController, friend: Friend) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(64.dp)
            .combinedClickable (
                onClick = {
                    navHostController.navigate(
                        Screen.FriendDetailScreen.createRoute(friend.userId)
                    )
                },
                onLongClick = {
                    menuExpanded = true
                }
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(64.dp)
        ) {
            val painter: Painter = rememberAsyncImagePainter(
                model = friend.profileUrl
            )

            Image(
                painter = painter,
                contentDescription = "${friend.nickname}의 프로필",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = friend.nickname,
                style = Typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                fontSize = 12.sp
            )
        }
    }

    if(showDeleteDialog){
        CustomDialog(
            title = "${friend.nickname}님을 삭제하시겠습니까?",
            content = "삭제하면 복구할 수 없습니다.",
            onConfirm = {
                // TODO: 친구 삭제
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false },
        offset = DpOffset(x = 64.dp, y = -50.dp)
    ) {
        DropdownMenuItem(
            text = { Text("친구 삭제") },
            onClick = {
                menuExpanded = false
                showDeleteDialog = true
            }
        )
    }
}