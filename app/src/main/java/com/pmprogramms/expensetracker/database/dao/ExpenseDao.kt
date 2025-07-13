package com.pmprogramms.expensetracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pmprogramms.expensetracker.model.Expense
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory

@Dao
interface ExpenseDao {
    @Query("select * from expenses")
    fun getAllExpenses(): LiveData<List<ExpenseWithCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: Expense)
}