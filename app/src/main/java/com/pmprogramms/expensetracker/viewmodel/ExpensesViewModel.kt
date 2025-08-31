package com.pmprogramms.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.pmprogramms.expensetracker.database.Database
import com.pmprogramms.expensetracker.database.state.DeleteState
import com.pmprogramms.expensetracker.database.state.UpdateState
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.helper.ExpenseFilter
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.repository.ExpensesRepository
import com.pmprogramms.expensetracker.utils.MathHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpensesViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ExpensesRepository

    private val _deleteState = MutableLiveData<DeleteState<Unit>>()
    val deleteState = _deleteState

    private val _updateState = MutableLiveData<UpdateState<Unit>>()
    val updateState = _updateState

    private val _expenseFilter: MutableLiveData<ExpenseFilter> = MutableLiveData<ExpenseFilter>()
    val expenseFilter get() = _expenseFilter

    val allExpenses: LiveData<List<ExpenseWithCategory>>

    init {
        val dao = Database.getDatabase(application).getExpensesDao()
        _expenseFilter.value = ExpenseFilter()

        repository = ExpensesRepository(dao)
        allExpenses = expenseFilter.switchMap { filter ->
            repository.getAllExpenses(filter.category?.categoryID, filter.valueFrom, filter.valueTo, filter.tsFrom, filter.tsTo, filter.type)
        }
    }

    fun insertExpense(name: String, value: Double, categoryId: Int?, expenseType: ExpenseType, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExpense(name, value, categoryId, expenseType)
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    fun getAllExpensesByRange(start: Long, end: Long): LiveData<Double> {
        val currentMonthExpenses = repository.getAllExpensesByRange(start, end)
        val totalAmount = MediatorLiveData<Double>()

        totalAmount.addSource(currentMonthExpenses) { data ->
            totalAmount.value = MathHelper.sumExpenses(data)
        }
        return totalAmount
    }

    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch {
            _deleteState.value = DeleteState.Loading
            repository.deleteExpense(expenseId).fold(
                onSuccess = { _deleteState.value = DeleteState.Success(Unit) },
                onFailure = { _deleteState.value = DeleteState.Error(it) }
            )
        }
    }

    fun updateExpense(id: Int, newName: String, newValue: Double, newCategoryID: Int?, newExpenseType: ExpenseType) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            repository.updateExpense(id, newName, newValue, newCategoryID, newExpenseType).fold(
                onSuccess = { _updateState.value = UpdateState.Success(Unit) },
                onFailure = { _updateState.value = UpdateState.Error(it) }
            )
        }
    }

    fun getExpenseById(expenseId: Int): LiveData<ExpenseWithCategory> {
        return repository.getExpenseById(expenseId)
    }

    fun setFilter(expenseFilter: ExpenseFilter) {
        _expenseFilter.value = expenseFilter
    }
}