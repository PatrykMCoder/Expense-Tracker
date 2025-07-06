package com.pmprogramms.expensetracker.repository

import androidx.lifecycle.LiveData
import com.pmprogramms.expensetracker.database.ExpenseDao
import com.pmprogramms.expensetracker.model.Expense

class ExpensesRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun insertExpense(name: String, value: Double, category: String) {
        val expense = Expense(0, name, value, category)
        expenseDao.insertExpense(expense)
    }

}