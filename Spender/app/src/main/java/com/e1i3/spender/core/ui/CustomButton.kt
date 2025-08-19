package com.e1i3.spender.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.e1i3.spender.ui.theme.LightPointColor
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.WhiteColor

@Composable
fun CustomLongButton(
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
        .height(56.dp)
        .fillMaxWidth()
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) PointColor else LightPointColor,
            contentColor = WhiteColor
        ),
        shape = RoundedCornerShape(16.dp),
        enabled = isEnabled
    ) {
        Text(
            text = text,
            style = Typography.titleMedium.copy(
                color = WhiteColor
            )
        )
    }
}


@Composable
fun CustomShortButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = WhiteColor
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = text,
            style = Typography.titleMedium.copy(
                color = WhiteColor
            )
        )
    }
}