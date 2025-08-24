package com.pmprogramms.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.pmprogramms.expensetracker.database.Database
import com.pmprogramms.expensetracker.database.state.DeleteState
import com.pmprogramms.expensetracker.database.state.UpdateState
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.repository.ExpensesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpensesViewModel(application: Application): AndroidViewModel(application) {
    val allExpenses: LiveData<List<ExpenseWithCategory>>
    private val repository: ExpensesRepository

    private val _deleteState = MutableLiveData<DeleteState<Unit>>()
    val deleteState = _deleteState

    private val _updateState = MutableLiveData<UpdateState<Unit>>()
    val updateState = _updateState

    init {
        val dao = Database.getDatabase(application).getExpensesDao()

        repository = ExpensesRepository(dao)
        allExpenses = repository.allExpenses
    }

    fun insertExpense(name: String, value: Double, categoryId: Int?, expenseType: ExpenseType, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExpense(name, value, categoryId, expenseType)
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    fun getCurrentMonthBalance(now: Int, firstDayInMonth: Int): LiveData<Double> {
        val currentMonthExpenses = repository.getAllExpensesByMonth(now, firstDayInMonth)
        val totalAmount = MediatorLiveData<Double>()

        totalAmount.addSource(currentMonthExpenses) { data ->
            totalAmount.value = data.sumOf {
                if (it.expense.expenseType == ExpenseType.IN) {
                    it.expense.value!!
                } else {
                    it.expense.value!! * -1
                }
            }
        }
        return totalAmount
    }

    fun getTodayBalance(now: Int, firstDayInMonth: Int): LiveData<Double> {
        val currentMonthExpenses = repository.getAllExpensesToday(now, firstDayInMonth)
        val totalAmount = MediatorLiveData<Double>()

        totalAmount.addSource(currentMonthExpenses) { data ->
            totalAmount.value = data.sumOf {
                if (it.expense.expenseType == ExpenseType.IN) {
                    it.expense.value!!
                } else {
                    it.expense.value!! * -1
                }
            }
        }
        return totalAmount
    }

    fun getWeekBalance(now: Int, firstDayInMonth: Int): LiveData<Double> {
        val currentMonthExpenses = repository.getAllExpensesByWeek(now, firstDayInMonth)
        val totalAmount = MediatorLiveData<Double>()

        totalAmount.addSource(currentMonthExpenses) { data ->
            totalAmount.value = data.sumOf {
                if (it.expense.expenseType == ExpenseType.IN) {
                    it.expense.value!!
                } else {
                    it.expense.value!! * -1
                }
            }
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

    fun updateExpense(id: Int, newName: String, newValue: Double, newCategoryID: Int?) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            repository.updateExpense(id, newName, newValue, newCategoryID).fold(
                onSuccess = { _updateState.value = UpdateState.Success(Unit) },
                onFailure = { _updateState.value = UpdateState.Error(it) }
            )
        }
    }

    fun getExpenseById(expenseId: Int): LiveData<ExpenseWithCategory> {
        return repository.getExpenseById(expenseId)
    }
}