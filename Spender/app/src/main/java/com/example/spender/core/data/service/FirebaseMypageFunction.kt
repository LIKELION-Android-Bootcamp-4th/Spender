package com.example.spender.core.data.service

import com.example.spender.core.data.remote.category.CategoryDto
import com.example.spender.core.data.remote.category.CategoryType
import com.example.spender.core.data.remote.expense.ExpenseDto
import com.example.spender.core.data.remote.expense.RegularExpenseDto
import com.example.spender.core.data.remote.expense.RegularExpenseType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 지출 카테고리
fun getOutcomeCategories(): MutableList<CategoryDto> {
    val uid = getFirebaseAuth()
    val categories = mutableListOf<CategoryDto>()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("categories")
            .whereEqualTo("type", CategoryType.OUTCOME.type)
        ref.get().addOnSuccessListener { document ->
            for (doc in document) {
                val data = doc.data
                val category = CategoryDto(
                    id = doc.id,
                    name = data["name"]?.toString() ?: "",
                    type = data["type"]?.toString() ?: CategoryType.OUTCOME.type,
                    color = data["color"]?.toString() ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
                categories.add(category)
            }
        }
    } catch (e: Exception) {
    }

    return categories
}

// 수입 카테고리
fun getIncomeCategories(): MutableList<CategoryDto> {
    val uid = getFirebaseAuth()
    val categories = mutableListOf<CategoryDto>()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("categories")
            .whereEqualTo("type", CategoryType.INCOME.type)
        ref.get().addOnSuccessListener { document ->
            for (doc in document) {
                val data = doc.data
                val category = CategoryDto(
                    id = doc.id,
                    name = data["name"]?.toString() ?: "",
                    type = data["type"]?.toString() ?: CategoryType.INCOME.type,
                    color = data["color"]?.toString() ?: "",
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
                categories.add(category)
            }
        }
    } catch (e: Exception) {
    }

    return categories
}

fun getRegularExpense(): MutableList<RegularExpenseDto> {
    val uid = getFirebaseAuth()
    val regularExpenses = mutableListOf<RegularExpenseDto>()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("regular_expenses")
        ref.get().addOnSuccessListener { document ->
            for (doc in document) {
                val data = doc.data
                val regularExpense = RegularExpenseDto (
                    id = doc.id,
                    amount = data["amount"]?.toString()?.toInt() ?: 0,
                    memo = data["memo"]?.toString() ?: "",
                    title = data["title"]?.toString() ?: "",
                    day = data["day"]?.toString()?.toInt() ?: 0,
                    firstPaymentDate = data["firstPaymentDate"] as? Timestamp ?: Timestamp.now(),
                    repeatCycle = data["repeatCycle"]?.toString() ?: RegularExpenseType.DAILY.type,
                    createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                )
                regularExpenses.add(regularExpense)
            }
        }
    } catch (e: Exception) {
    }

    return regularExpenses
}


// setter

fun setRegularExpense(regularExpense: RegularExpenseDto) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("regular_expenses").document()
        ref.set(regularExpense)
    } catch (e: Exception) {
    }
}

// 지출 등록
// 이미지 업로드는 처리되어 있지 않음
fun setExpense(expenseDto: ExpenseDto) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("expenses").document()
        ref.set(expenseDto)
    } catch (e: Exception) {
    }
}

// 수입 등록
fun setIncome(incomeDto: ExpenseDto) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("incomes").document()
        val income = mapOf(
            "title" to incomeDto.title,
            "amount" to incomeDto.amount,
            "memo" to incomeDto.memo,
            "date" to incomeDto.date,
            "categoryId" to incomeDto.categoryId,
            "createdAt" to incomeDto.createdAt
        )
        ref.set(income)
    } catch (e: Exception) {
    }
}

fun setCategory(category: CategoryDto) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("categories").document()
        ref.set(category)
    } catch (e: Exception) {
    }
}

// 예산은 수정할 경우 그냥 새 budget 문서를 만들어서 쌓고, 가져올 때 가장 최근 것만 가져오는 식으로 동작
fun setUserBudget(budget: Int) {
    val uid = getFirebaseAuth()
    val timeStamp = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date(System.currentTimeMillis()))
    try {
        val ref = getFirebaseRef().document(uid!!).collection("budgets").document(timeStamp)
        val budgetInfo = mapOf(
            "amount" to budget,
            "createdAt" to FieldValue.serverTimestamp()
        )
        ref.set(budgetInfo)
    } catch (e: Exception) {
    }
}

// 이하는 수정
fun editRegularExpense(regularExpense: RegularExpenseDto, id: String) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("regular_expenses").document(id)
        ref.set(regularExpense)
    } catch (e: Exception) {
    }
}

fun editExpense(expense: ExpenseDto, id: String) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("expenses").document(id)
        ref.set(expense)
    } catch (e: Exception) {
    }
}

fun editIncome(incomeDto: ExpenseDto, id: String) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("incomes").document(id)
        val income = mapOf(
            "title" to incomeDto.title,
            "amount" to incomeDto.amount,
            "memo" to incomeDto.memo,
            "date" to incomeDto.date,
            "categoryId" to incomeDto.categoryId,
            "createdAt" to incomeDto.createdAt
        )
        ref.set(income)
    } catch (e: Exception) {
    }
}

fun editCategory(category: CategoryDto, id: String) {
    val uid = getFirebaseAuth()

    try {
        val ref = getFirebaseRef().document(uid!!).collection("categories").document(id)
        ref.set(category)
    } catch (e: Exception) {
    }
}