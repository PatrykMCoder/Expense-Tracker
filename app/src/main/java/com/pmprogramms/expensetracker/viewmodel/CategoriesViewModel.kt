package com.pmprogramms.expensetracker.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pmprogramms.expensetracker.database.Database
import com.pmprogramms.expensetracker.database.state.DeleteState
import com.pmprogramms.expensetracker.model.Category
import com.pmprogramms.expensetracker.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesViewModel(application: Application): AndroidViewModel(application) {
    val allCategories: LiveData<List<Category>>
    private val repository: CategoriesRepository
    private val _deleteState = MutableLiveData<DeleteState<Unit>>()
    val deleteState = _deleteState

    init {
        val dao = Database.getDatabase(application).getCategoriesDao()

        repository = CategoriesRepository(dao)
        allCategories = repository.allCategories
    }

    fun insertCategories(name: String, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCategory(name)
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    fun deleteCategory(categoryID: Int) {
        viewModelScope.launch {
            _deleteState.value = DeleteState.Loading
            repository.deleteCategory(categoryID).fold(
                onSuccess = { _deleteState.value = DeleteState.Success(Unit) },
                onFailure = { _deleteState.value = DeleteState.Error(it) }
            )
        }
    }
}