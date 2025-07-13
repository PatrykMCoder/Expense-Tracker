package com.pmprogramms.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pmprogramms.expensetracker.database.Database
import com.pmprogramms.expensetracker.model.Category
import com.pmprogramms.expensetracker.model.Expense
import com.pmprogramms.expensetracker.repository.CategoriesRepository
import com.pmprogramms.expensetracker.repository.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application): AndroidViewModel(application) {
    val allCategories: LiveData<List<Category>>
    private val repository: CategoriesRepository

    init {
        val dao = Database.getDatabase(application).getCategoriesDao()

        repository = CategoriesRepository(dao)
        allCategories = repository.allCategories
    }

    fun insertCategories(name: String, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCategory(name)
            onComplete()
        }
    }
}