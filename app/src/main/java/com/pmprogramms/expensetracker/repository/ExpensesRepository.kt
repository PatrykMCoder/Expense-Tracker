package com.pmprogramms.expensetracker.repository

import androidx.lifecycle.LiveData
import com.pmprogramms.expensetracker.database.dao.ExpenseDao
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.Expense
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory

class ExpensesRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: LiveData<List<ExpenseWithCategory>> = expenseDao.getAllExpenses()

    suspend fun insertExpense(name: String, value: Double, categoryId: Int?, expenseType: ExpenseType) {
        val expense = Expense(0, name, value, categoryId, expenseType, System.currentTimeMillis())
        expenseDao.insertExpense(expense)
    }

    fun getAllExpensesByMonth(now: Int, firstDayInMonth: Int): LiveData<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesByMonth()
    }

    fun getAllExpensesByWeek(now: Int, firstDayInMonth: Int): LiveData<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesByWeek()
    }

    fun getAllExpensesToday(now: Int, firstDayInMonth: Int): LiveData<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesToday()
    }

    suspend fun deleteExpense(expenseID: Int): Result<Unit> {
        return try {
            expenseDao.deleteExpenseByID(expenseID)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExpense(id: Int, newName: String, newValue: Double, newCategoryID: Int?): Result<Unit> {
        return try {
            expenseDao.updateCategoryNameById(id, newName, newValue, newCategoryID)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getExpenseById(expenseID: Int): LiveData<ExpenseWithCategory> = expenseDao.getExpenseByID(expenseID)

}