package com.pmprogramms.expensetracker.repository

import androidx.lifecycle.LiveData
import com.pmprogramms.expensetracker.database.dao.CategoryDao
import com.pmprogramms.expensetracker.model.Category

class CategoriesRepository(private val categoryDao: CategoryDao) {
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(name: String) {
        val category = Category(0, name)
        categoryDao.insertCategory(category)
    }

}