package com.pmprogramms.expensetracker.database.state

sealed class DeleteState<out T> {
    data object Loading: DeleteState<Nothing>()
    data class Success<T>(val data: T) : DeleteState<T>()
    data class Error(val error: Throwable) : DeleteState<Nothing>()

}