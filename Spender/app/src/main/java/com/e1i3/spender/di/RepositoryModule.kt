package com.e1i3.spender.di

import com.e1i3.spender.feature.expense.data.repository.ExpenseRepository
import com.e1i3.spender.feature.expense.data.repository.RegularExpenseRepository
import com.e1i3.spender.feature.income.data.repository.IncomeRepository
import com.e1i3.spender.feature.mypage.data.repository.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideExpenseRepository(): ExpenseRepository {
        return ExpenseRepository()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(): CategoryRepository {
        return CategoryRepository()
    }

    @Provides
    @Singleton
    fun provideIncomeRepository(): IncomeRepository {
        return IncomeRepository()
    }

    @Provides
    @Singleton
    fun provideRegularExpenseRepository(): RegularExpenseRepository {
        return RegularExpenseRepository()
    }
}