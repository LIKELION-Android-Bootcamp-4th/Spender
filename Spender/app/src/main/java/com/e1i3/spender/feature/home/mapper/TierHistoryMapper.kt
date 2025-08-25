package com.e1i3.spender.feature.home.mapper

import com.e1i3.spender.core.data.remote.tier.TierHistoryDto
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toTierHistoryDto(): TierHistoryDto {
    return TierHistoryDto(
        month = this.id,
        level = (this.get("level") as? Number)?.toInt() ?: 3
    )
}