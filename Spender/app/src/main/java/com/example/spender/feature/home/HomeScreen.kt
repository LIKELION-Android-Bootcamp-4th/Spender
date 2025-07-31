package com.example.spender.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.spender.core.ui.MyTypography

@Composable
fun HomeScreen(navHostController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
        Text("titleLarge", style = MyTypography.titleLarge)
        Text("titleMedium", style = MyTypography.titleMedium)
        Text("titleSmall", style = MyTypography.titleSmall)

        Text("bodyLarge", style = MyTypography.bodyLarge)
        Text("bodyMedium", style = MyTypography.bodyMedium)
        Text("bodySmall", style = MyTypography.bodySmall)

        Text("labelLarge", style = MyTypography.labelLarge)
        Text("labelMedium", style = MyTypography.labelMedium)
        Text("labelSmall", style = MyTypography.labelSmall)


    }
}