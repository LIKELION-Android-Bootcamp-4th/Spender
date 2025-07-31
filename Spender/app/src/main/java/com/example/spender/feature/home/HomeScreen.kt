package com.example.spender.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.spender.ui.theme.Typography

@Composable
fun HomeScreen(navHostController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
        Text("titleLarge", style = Typography.titleLarge)
        Text("titleMedium", style = Typography.titleMedium)
        Text("titleSmall", style = Typography.titleSmall)

        Text("bodyLarge", style = Typography.bodyLarge)
        Text("bodyMedium", style = Typography.bodyMedium)
        Text("bodySmall", style = Typography.bodySmall)

        Text("labelLarge", style = Typography.labelLarge)
        Text("labelMedium", style = Typography.labelMedium)
        Text("labelSmall", style = Typography.labelSmall)


    }
}