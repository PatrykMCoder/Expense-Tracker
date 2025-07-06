package com.pmprogramms.expensetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pmprogramms.expensetracker.model.Expense

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun getExpensesDao(): ExpenseDao

    companion object {
        private var DATABASE_INSTANCE: com.pmprogramms.expensetracker.database.Database? = null

        fun getDatabase(context: Context): com.pmprogramms.expensetracker.database.Database {
            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.pmprogramms.expensetracker.database.Database::class.java,
                    "expenses_table"
                ).build()
                DATABASE_INSTANCE = instance

                instance
            }
        }
    }
}