package com.pmprogramms.expensetracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.Expense
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory

@Dao
interface ExpenseDao {
    @Query("select * from expenses")
    fun getAllExpenses(): LiveData<List<ExpenseWithCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: Expense)

    @Query("select * from expenses where created_at between :start and :end")
    fun getAllExpensesByRange(start: Long, end: Long): LiveData<List<ExpenseWithCategory>>

    @Query("DELETE from expenses where uid = :id")
    suspend fun deleteExpenseByID(id: Int)

    @Query("SELECT * from expenses where uid = :id")
    fun getExpenseByID(id: Int): LiveData<ExpenseWithCategory>

    @Query("UPDATE expenses SET name = :newName, value = :newValue, category_id = :newCategoryID, type = :newExpenseType where uid = :id")
    suspend fun updateCategoryNameById(id: Int, newName: String, newValue: Double, newCategoryID: Int?, newExpenseType: ExpenseType)
}