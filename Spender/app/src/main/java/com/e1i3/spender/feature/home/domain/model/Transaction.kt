package com.e1i3.spender.feature.home.domain.model

import java.util.Date

data class Transaction(
    val id: String,
    val title: String,
    val amount: Long,
    val type: String,
    val date: Date
)