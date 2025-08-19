package com.example.spender.core.common.util

fun Int.toCurrency(): String {
    return "%,d".format(this)
}

fun formatToManWon(amount: Int): String {
    val man = amount / 10_000

    return "${man}만원"
}