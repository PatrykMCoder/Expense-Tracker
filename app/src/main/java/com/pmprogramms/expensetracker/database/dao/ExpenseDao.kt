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

//    @Query("select * from expenses where createdAt between :firstDayInMonth and :now")
    @Query("select * from expenses")
    fun getAllExpensesByMonth(): LiveData<List<ExpenseWithCategory>>

    @Query("select * from expenses")
    fun getAllExpensesByWeek(): LiveData<List<ExpenseWithCategory>>

    @Query("select * from expenses")
    fun getAllExpensesToday(): LiveData<List<ExpenseWithCategory>>

}