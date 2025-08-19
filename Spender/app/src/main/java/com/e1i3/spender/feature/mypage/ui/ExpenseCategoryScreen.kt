package com.e1i3.spender.feature.mypage.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.e1i3.spender.core.ui.CustomTopAppBar
import com.e1i3.spender.feature.mypage.domain.model.Category
import com.e1i3.spender.feature.mypage.ui.component.CategoryDeleteDialog
import com.e1i3.spender.feature.mypage.ui.component.CategoryEditDialog
import com.e1i3.spender.feature.mypage.ui.viewmodel.CategoryViewModel
import com.e1i3.spender.ui.theme.PointColor
import com.e1i3.spender.ui.theme.Typography
import com.e1i3.spender.ui.theme.WhiteColor

@Composable
fun ExpenseCategoryScreen(
    navHostController: NavHostController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadCategories("EXPENSE")
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var currentCategory by remember { mutableStateOf<Category?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "지출 카테고리",
                navHostController,
                showBackButton = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (categories.size >= 10) {
                        Toast.makeText(context, "카테고리는 최대 10개까지 추가할 수 있습니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        isEditing = false
                        currentCategory = null
                        showEditDialog = true
                    }
                },
                containerColor = PointColor,
                shape = RoundedCornerShape(72.dp),
            ) {
                Icon(
                    imageVector = if (showEditDialog) Icons.Rounded.Close else Icons.Rounded.Add,
                    contentDescription = "카테고리 추가",
                    modifier = if (showEditDialog) Modifier.size(35.dp) else Modifier.size(40.dp),
                    tint = WhiteColor,
                )
            }
        },
        content = { padding ->

            LazyColumn(modifier = Modifier.padding(padding)) {
                items(categories, key = { it.id }) { category ->
                    CategoryRow(
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
                            viewModel.addCategory(newName, "EXPENSE")
                        }
                        showEditDialog = false
                    }
                )
            }

            // 삭제 확인 다이얼로그
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
private fun CategoryRow(
    category: Category,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /*   */ }
            .padding(horizontal = 46.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        category.color?.let {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(it)
            )
            Spacer(Modifier.width(16.dp))
        }
        Text(
            text = category.name,
            style = Typography.titleMedium,
        )
        Spacer(Modifier.weight(1f))

        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "더보기")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
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
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.tertiary)
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