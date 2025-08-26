package com.e1i3.spender.feature.analysis.mapper

fun emotionIdToString(id: String): String {
    return when(id) {
        "satisfied" -> "#만족"
        "dissatisfied" -> "#불만"
        "impulsive" -> "#충동"
        "unfair" -> "#억울"
        else -> ""
    }
}