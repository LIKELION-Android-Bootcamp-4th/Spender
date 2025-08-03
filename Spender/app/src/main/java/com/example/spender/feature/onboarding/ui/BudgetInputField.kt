package com.example.spender.feature.onboarding.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.spender.core.common.util.CurrencyVisualTransformation
import com.example.spender.ui.theme.LightSurface
import com.example.spender.ui.theme.TabColor
import com.example.spender.ui.theme.Typography

@Composable
fun BudgetInputField(
    budget: Int,
    onBudgetChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TextField(
            value = budget?.takeIf { it != 0 }?.toString() ?: "",
            onValueChange = { input ->
                val numbersOnly = input.filter { it.isDigit() }
                val intBudget = numbersOnly.toIntOrNull() ?: 0
                onBudgetChange(intBudget)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LightSurface,
                unfocusedContainerColor = LightSurface,
                focusedIndicatorColor = TabColor,
                unfocusedIndicatorColor = TabColor,
                cursorColor = TabColor,
            ),
            textStyle = Typography.titleLarge,
            placeholder = {
                Text(
                    "예산을 입력해주세요",
                    style = Typography.titleLarge.copy(
                        color = TabColor
                    )
                )
            },
            visualTransformation = CurrencyVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "원",
            style = Typography.titleLarge
        )
    }
}