package com.example.spender.core.widget.component

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.example.spender.core.widget.SpenderMediumWidget
import com.example.spender.core.widget.SpenderSmallWidget

class RefreshExpenseAction : ActionCallback {
    companion object {
        val KeyWidget = ActionParameters.Key<String>("widget")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        when (parameters[KeyWidget]) {
            "small" -> SpenderSmallWidget().update(context, glanceId)
            "medium" -> SpenderMediumWidget().update(context, glanceId)
        }
    }
}