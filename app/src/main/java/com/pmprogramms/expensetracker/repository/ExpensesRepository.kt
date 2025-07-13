package com.pmprogramms.expensetracker.repository

import androidx.lifecycle.LiveData
import com.pmprogramms.expensetracker.database.dao.ExpenseDao
import com.pmprogramms.expensetracker.model.Expense
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory

class ExpensesRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: LiveData<List<ExpenseWithCategory>> = expenseDao.getAllExpenses()

    suspend fun insertExpense(name: String, value: Double, categoryId: Int) {
        val expense = Expense(0, name, value, categoryId)
        expenseDao.insertExpense(expense)
    }

}