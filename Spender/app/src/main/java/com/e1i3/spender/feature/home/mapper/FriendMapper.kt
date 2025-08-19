package com.e1i3.spender.feature.home.mapper

import com.e1i3.spender.core.data.remote.friend.FriendListDto
import com.e1i3.spender.feature.home.domain.model.Friend

fun FriendListDto.toDomain(userId: String): Friend {
    return Friend(
        userId = userId,
        nickname = nickname,
        profileUrl = profileUrl,
        status = status,
    )
}
