package com.e1i3.spender.feature.home.ui

import com.e1i3.spender.feature.home.domain.model.Transaction

data class SearchUiState(
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0,
    val searchResults: List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)