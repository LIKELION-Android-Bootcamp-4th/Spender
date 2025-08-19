package com.e1i3.spender.core.widget.component

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.e1i3.spender.core.widget.SpenderMediumWidget
import com.e1i3.spender.core.widget.SpenderSmallWidget

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