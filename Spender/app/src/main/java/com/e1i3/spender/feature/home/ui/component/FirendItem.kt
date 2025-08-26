package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.feature.mypage.ui.component.CircularImage
import com.e1i3.spender.ui.theme.LightPointColor
import com.e1i3.spender.ui.theme.Typography
import kotlin.math.roundToInt

@Composable
fun FriendItem(
    friend: Friend,
    onClick: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    var itemWindowBounds by remember { mutableStateOf(Rect.Zero) }
    var longPressLocalOffset by remember { mutableStateOf(Offset.Zero) }
    var popupOffset by remember { mutableStateOf(IntOffset.Zero) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .onGloballyPositioned { coords ->
                itemWindowBounds = coords.boundsInWindow()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onClick() },
                        onLongPress = { offset ->
                            longPressLocalOffset = offset
                            val xPx = itemWindowBounds.left + longPressLocalOffset.x
                            val yPx = itemWindowBounds.top + longPressLocalOffset.y
                            popupOffset = IntOffset(xPx.roundToInt(), yPx.roundToInt())
                            menuExpanded = true
                        }
                    )
                }
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
            offset = popupOffset.copy(
                x = popupOffset.x + with(density) { 8.dp.toPx() }.roundToInt(),
                y = popupOffset.y - with(density) { 60.dp.toPx() }.roundToInt()
            ),
            onDismissRequest = { menuExpanded = false },
            properties = PopupProperties(focusable = true)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = LightPointColor.copy(alpha = 0.92f))
            ) {
                Column {
                    Text(
                        "친구 삭제",
                        modifier = Modifier
                            .clickable {
                                onDeleteRequest()
                                menuExpanded = false
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        style = Typography.titleSmall,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        maxLines = 1
                    )
                }
            }
        }
    }
}