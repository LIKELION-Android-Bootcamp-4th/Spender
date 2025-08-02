package com.example.spender.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spender.ui.theme.LightPointColor
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography
import com.example.spender.ui.theme.WhiteColor

@Composable
fun CustomLongButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .height(56.dp)
        .fillMaxWidth()
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = PointColor,
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


@Preview
@Composable
fun CustomLongButtonPreview() {
    CustomLongButton(
        text = "버튼이름",
        onClick = {}
    )
}

@Preview
@Composable
fun CustomShortButtonPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        CustomShortButton(
            text = "버튼1 이름 ",
            backgroundColor = LightPointColor,
            onClick = {},
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        CustomShortButton(
            text = "버튼2 이름",
            backgroundColor = PointColor,
            onClick = {},
            modifier = Modifier.weight(1f)
        )
    }
}