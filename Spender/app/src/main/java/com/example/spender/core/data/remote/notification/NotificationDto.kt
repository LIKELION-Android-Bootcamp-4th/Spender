package com.example.spender.core.data.remote.notification

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class NotificationDto (
    val title : String = "",
    val content: String = "",
    val createdAt: Timestamp? = null,

    @get:PropertyName("notificationType")
    @set:PropertyName("notificationType")
    var type: String = "",

    val route: String = "",
    val isRead: Boolean
)