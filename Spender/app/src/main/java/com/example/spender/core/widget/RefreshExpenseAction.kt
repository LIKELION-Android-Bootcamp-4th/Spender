package com.example.spender.core.widget

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

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