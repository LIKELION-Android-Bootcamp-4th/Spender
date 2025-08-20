package com.e1i3.spender.feature.home.ui.component

import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.e1i3.spender.core.ui.CustomDialog
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.home.ui.viewModel.HomeViewModel
import com.e1i3.spender.ui.theme.LightPointColor
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.navigation.Screen
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendItem(navHostController: NavHostController, friend: Friend, viewModel: HomeViewModel) {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .width(64.dp)
            .onGloballyPositioned { coordinates ->
                val windowPos = coordinates.localToWindow(Offset.Zero)
                itemPosition = windowPos
            }
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
            content = "삭제하더라도 나중에 다시 친구 추가가 가능해요.",
            onConfirm = {
                viewModel.deleteFriend(friendId = friend.userId)
                viewModel.getFriendList()
                showDeleteDialog = false
                Toast.makeText(context, "${friend.nickname}님이 삭제되었습니다.", Toast.LENGTH_SHORT)
                    .show()
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    if (menuExpanded) {
        Popup(
            offset = IntOffset(itemPosition.x.roundToInt() + 64, itemPosition.y.roundToInt() - 60),
            onDismissRequest = { menuExpanded = false },
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = LightPointColor.copy(alpha = 0.85f),)
                    .padding(vertical = 4.dp)
            ) {
                Column {
                    Text(
                        "친구 삭제",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDeleteDialog = true
                                menuExpanded = false
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .align(Alignment.CenterHorizontally),
                        style = Typography.titleSmall,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}