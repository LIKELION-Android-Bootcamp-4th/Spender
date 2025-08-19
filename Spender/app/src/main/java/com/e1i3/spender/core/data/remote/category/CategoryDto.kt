package com.e1i3.spender.core.data.remote.category

import com.google.firebase.Timestamp

data class CategoryDto(
    val id: String,
    val name: String,
    val type: String,
    val color: String,
    val createdAt: Timestamp
)
