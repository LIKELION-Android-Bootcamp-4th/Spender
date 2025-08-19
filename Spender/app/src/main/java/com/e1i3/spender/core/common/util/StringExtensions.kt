package com.e1i3.spender.core.common.util

fun String.toYearMonth(): String {
    return try {
        val parts = this.split("-")
        val year = parts[0]
        val month = parts[1]
        "${year}년 ${month}월"
    } catch (e: Exception) {
        this
    }
}