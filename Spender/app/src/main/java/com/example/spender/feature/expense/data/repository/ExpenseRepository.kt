package com.example.spender.feature.expense.data.repository

import com.example.spender.feature.expense.data.remote.ExpenseDto

interface ExpenseRepository {
    fun addExpense(userId: String, expense: ExpenseDto)
}