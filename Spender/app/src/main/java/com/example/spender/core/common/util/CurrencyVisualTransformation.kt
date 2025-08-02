package com.example.spender.core.common.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text.filter { it.isDigit() }

        if (rawText.isEmpty()) {
            return TransformedText(
                AnnotatedString(""),
                OffsetMapping.Identity
            )
        }

        val number = rawText.toLongOrNull() ?: 0L
        val formatted = "%,d".format(number)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset >= rawText.length) return formatted.length

                val beforeOffset = rawText.substring(0, offset)
                val transformedBefore = "%,d".format(beforeOffset.toLongOrNull() ?: 0L)
                return transformedBefore.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                val digitsBeforeOffset = formatted.substring(0, offset.coerceAtMost(formatted.length))
                    .filter { it.isDigit() }
                return digitsBeforeOffset.length
            }
        }

        return TransformedText(
            AnnotatedString(formatted),
            offsetMapping
        )
    }
}