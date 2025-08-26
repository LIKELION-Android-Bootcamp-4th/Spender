package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.mypage.ui.component.CircularImage
import com.e1i3.spender.ui.theme.LightPointColor
import com.e1i3.spender.ui.theme.Typography
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendItem(
    friend: Friend, onClick: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val itemPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .width(64.dp)
            .padding(bottom = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(64.dp)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { menuExpanded = true })
        ) {
            CircularImage(
                profileUrl = friend.photoUrl,
                size = 60.dp
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

    if (menuExpanded) {
        Popup(
            offset = IntOffset(itemPosition.x.roundToInt() + 64, itemPosition.y.roundToInt() - 60),
            onDismissRequest = { menuExpanded = false },
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = LightPointColor.copy(alpha = 0.85f))
                    .padding(vertical = 4.dp)
            ) {
                Column {
                    Text(
                        "친구 삭제",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDeleteRequest()
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