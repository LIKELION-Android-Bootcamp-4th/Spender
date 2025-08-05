package com.example.spender.core.common.util

fun Int.toCurrency(): String {
    return "%,d".format(this)
}

fun formatToManWon(amount: Int): String {
    val man = amount.toDouble() / 10_000

//    return if (amount % 10_000 == 0) {
//        "${man.toInt()}만원"
//    } else {
//        String.format("%.1f만원", man)
//    }
    return "${man}만원"
}