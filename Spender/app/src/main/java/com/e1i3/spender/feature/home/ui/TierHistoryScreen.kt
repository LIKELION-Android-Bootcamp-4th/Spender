package com.e1i3.spender.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.LoadingScreen
import com.e1i3.spender.core.ui.TierDialog
import com.e1i3.spender.feature.home.ui.component.EmptyTier
import com.e1i3.spender.feature.home.ui.component.TierItem
import com.e1i3.spender.feature.home.ui.viewModel.TierHistoryViewModel
import com.e1i3.spender.feature.report.ui.component.ReportTopAppBar
import com.e1i3.spender.feature.report.ui.component.YearPickerDialog
import java.util.Calendar

@Composable
fun TierHistoryScreen(
    navHostController: NavHostController,
    viewModel: TierHistoryViewModel = hiltViewModel()
) {
    val year by viewModel.currentYear
    val currentYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val tiers by viewModel.tierList

    var showYearDialog by remember { mutableStateOf(false) }
    var showTierDialog by remember { mutableStateOf(false) }

    LaunchedEffect(year) {
        viewModel.loadTiers(year)
    }

    Scaffold(
        topBar = {
            ReportTopAppBar(
                year = year,
                onPrev = { viewModel.goToPreviousYear() },
                onNext = { viewModel.goToNextYear() },
                onYearClick = {
                    showYearDialog = true
                },
                navController = navHostController,
                showBackButton = true,
                actions = {
                    IconButton(onClick = {
                        showTierDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                            contentDescription = "티어 설명보기"
                        )
                    }
                }
            )
        },
        content = { padding ->
            when {
                viewModel.isLoading.value -> LoadingScreen()

                tiers.isEmpty() -> EmptyTier(paddingValues = padding)

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(tiers) { tier ->
                            TierItem(tier = tier)
                        }
                    }
                }
            }
        }
    )

    if (showYearDialog) {
        YearPickerDialog(
            currentYear = currentYear,
            selectedYear = year,
            onYearSelected = {
                viewModel.setYear(it)
                showYearDialog = false
            },
            onDismiss = { showYearDialog = false }
        )
    }
    
    if(showTierDialog){
        TierDialog(
            onDismiss = { showTierDialog = false }
        )
    }
}