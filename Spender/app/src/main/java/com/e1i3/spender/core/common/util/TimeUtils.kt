package com.e1i3.spender.core.common.util

import java.util.*
import java.util.concurrent.TimeUnit

fun getRelativeTimeString(date: Date): String {
    val now = Date()
    val diffInMillis = now.time - date.time
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        hours < 24 -> "${hours}시간 전"
        days == 1L -> "1일 전"
        days < 7 -> "${days}일 전"
        else -> {
            val calendar = Calendar.getInstance().apply { time = date }
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            "${month}월 ${day}일"
        }
    }
}
