package com.example.spender.feature.mypage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.example.spender.R
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spender.core.ui.CustomTopAppBar
import com.example.spender.feature.mypage.ui.component.CategoryDeleteDialog
import com.example.spender.feature.mypage.ui.component.CategoryEditDialog
import com.example.spender.feature.mypage.domain.model.Category
import com.example.spender.feature.mypage.ui.viewmodel.CategoryViewModel
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeCategoryScreen(
    navHostController: NavHostController,
    viewModel: CategoryViewModel = hiltViewModel()
) {

    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadCategories("INCOME")
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var currentCategory by remember { mutableStateOf<Category?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "수입 카테고리",
                navHostController,
                showBackButton = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isEditing = false
                    currentCategory = null
                    showEditDialog = true
                },
                containerColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_floating_add),
                    contentDescription = "카테고리 추가",
                    tint = PointColor,
                )
            }
        },
        content = { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(categories, key = { it.id }) { category ->
                    IncomeCategoryRow(
                        category = category,
                        onEditClick = {
                            isEditing = true
                            currentCategory = category
                            showEditDialog = true
                        },
                        onDeleteClick = {
                            currentCategory = category
                            showDeleteDialog = true
                        }
                    )
                    HorizontalDivider()
                }
            }
            // 수정/추가 다이얼로그
            if (showEditDialog) {
                CategoryEditDialog(
                    isEditing = isEditing,
                    category = currentCategory,
                    onDismiss = { showEditDialog = false },
                    onConfirm = { newName ->
                        if (isEditing) {
                            currentCategory?.let { viewModel.updateCategory(it.id, newName) }
                        } else {
                            viewModel.addCategory(newName, "INCOME")
                        }
                        showEditDialog = false
                    }
                )
            }
            if (showDeleteDialog) {
                CategoryDeleteDialog(
                    category = currentCategory,
                    onDismiss = { showDeleteDialog = false },
                    onConfirm = {
                        currentCategory?.let { viewModel.deleteCategory(it.id) }
                        showDeleteDialog = false
                    }
                )
            }
        }
    )
}

// 카테고리 한 줄
@Composable
private fun IncomeCategoryRow(
    category: Category,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /*  */ }
            .padding(horizontal = 46.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = category.name,
            style = Typography.titleMedium,
        )
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "더보기")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "수정하기",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = Typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        onEditClick()
                        expanded = false
                    },
                    modifier = Modifier
                        .height(34.dp)
                        .width(120.dp),
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color.Gray)
                DropdownMenuItem(
                    text = {
                        Text(
                            "삭제하기",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = Typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        onDeleteClick()
                        expanded = false
                    },
                    modifier = Modifier
                        .height(34.dp)
                        .width(120.dp),
                )
            }
        }
    }
}