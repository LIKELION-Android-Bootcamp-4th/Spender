package com.e1i3.spender.feature.mypage.domain.model

data class User(
    val displayName: String = "",
    val providerIcon: Int? = null,
    val displayEmail: String = "",
    val displayNickname: String = "",
    val profileUrl: String? = null
)