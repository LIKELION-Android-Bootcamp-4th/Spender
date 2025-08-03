package com.example.spender.core.common.util

fun Int.toCurrency(): String {
    return "%,d".format(this)
}

fun formatToManWon(amount: Int): String {
    val man = amount / 10_000
    val remain = amount % 10_000

    return if (remain == 0) {
        "${man}만원"
    } else {
        val fractional = String.format("$.1f", amount / 10_000)
        "${fractional}만원"
    }
}