package com.pmprogramms.expensetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pmprogramms.expensetracker.database.dao.CategoryDao
import com.pmprogramms.expensetracker.database.dao.ExpenseDao
import com.pmprogramms.expensetracker.model.Category
import com.pmprogramms.expensetracker.model.Expense

@Database(entities = [Expense::class, Category::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun getExpensesDao(): ExpenseDao
    abstract fun getCategoriesDao(): CategoryDao

    companion object {
        private var DATABASE_INSTANCE: com.pmprogramms.expensetracker.database.Database? = null

        fun getDatabase(context: Context): com.pmprogramms.expensetracker.database.Database {
            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.pmprogramms.expensetracker.database.Database::class.java,
                    "expenses_database"
                ).build()
                DATABASE_INSTANCE = instance

                instance
            }
        }
    }
}