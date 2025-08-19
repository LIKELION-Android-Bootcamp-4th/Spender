package com.e1i3.spender.core.data.remote.friend

import com.google.firebase.Timestamp

data class FriendListDto(
    val nickname: String = "",
    val profileUrl: String = "",
    val status: String = "FRIEND",
    val connectedAt: Timestamp = Timestamp.now()
)