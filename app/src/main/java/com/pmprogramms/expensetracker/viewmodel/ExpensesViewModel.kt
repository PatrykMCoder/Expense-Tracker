package com.pmprogramms.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pmprogramms.expensetracker.database.Database
import com.pmprogramms.expensetracker.model.Expense
import com.pmprogramms.expensetracker.repository.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpensesViewModel(application: Application): AndroidViewModel(application) {
    val allExpenses: LiveData<List<Expense>>
    private val repository: ExpensesRepository

    init {
        val dao = Database.getDatabase(application).getExpensesDao()

        repository = ExpensesRepository(dao)
        allExpenses = repository.allExpenses
    }

    fun insertExpense(name: String, value: Double, category: String, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExpense(name, value, category)
            onComplete()
        }
    }
}