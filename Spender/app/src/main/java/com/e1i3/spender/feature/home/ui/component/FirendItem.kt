package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.e1i3.spender.feature.home.domain.model.Friend
import com.e1i3.spender.ui.theme.Typography

@Composable
fun FriendItem(navHostController: NavHostController, friend: Friend){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .clickable{
                // TODO: 친구 상세 화면으로 이동
                navHostController.navigate("friend_detail")
            }
    ) {
        val painter: Painter = rememberAsyncImagePainter(
            model = friend.profileUrl
        )

        Image(
            painter = painter,
            contentDescription = "${friend.nickname}의 프로필",
            modifier = Modifier.size(60.dp).clip(CircleShape),
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