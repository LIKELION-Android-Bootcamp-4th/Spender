package com.e1i3.spender.core.data.remote.friend

data class FriendListDto(
    val nickname: String = "알 수 없음",
    val profileUrl: String = "https://firebasestorage.googleapis.com/v0/b/spender-5f3c4.firebasestorage.app/o/spender.png?alt=media&token=30002d1f-9c06-44d6-9492-e8521f2a94f2",
    val status: String = "FRIEND",
)