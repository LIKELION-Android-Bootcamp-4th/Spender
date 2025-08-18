package com.example.spender.feature.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.spender.R
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
    Column(
        modifier = Modifier.padding(16.dp)
    ){
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
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                ),
                textStyle = Typography.titleLarge,
                placeholder = {
                    Text(
                        "예산을 입력해주세요",
                        style = Typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary
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

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.warning_icon),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "예산은 언제든 변경할 수 있어요",
                style = Typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}