package com.example.spender.core.widget.component

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.example.spender.core.widget.SpenderMediumWidget

class RefreshExpenseAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("RefreshExpenseAction", "새로고침 버튼 클릭됨")

        SpenderMediumWidget().update(context, glanceId)
    }
}