package com.pmprogramms.expensetracker.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.pmprogramms.expensetracker.database.dao.CategoryDao
import com.pmprogramms.expensetracker.model.Category

class CategoriesRepository(private val categoryDao: CategoryDao) {
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(name: String) {
        val category = Category(null, name)
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(categoryID: Int): Result<Unit> {
        return try {
            categoryDao.deleteCategoryByID(categoryID)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
