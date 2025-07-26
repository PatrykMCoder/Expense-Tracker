package com.pmprogramms.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pmprogramms.expensetracker.database.Database
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.repository.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpensesViewModel(application: Application): AndroidViewModel(application) {
    val allExpenses: LiveData<List<ExpenseWithCategory>>
    private val repository: ExpensesRepository

    init {
        val dao = Database.getDatabase(application).getExpensesDao()

        repository = ExpensesRepository(dao)
        allExpenses = repository.allExpenses
    }

    fun insertExpense(name: String, value: Double, categoryId: Int, expenseType: ExpenseType, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExpense(name, value, categoryId, expenseType)
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }
}