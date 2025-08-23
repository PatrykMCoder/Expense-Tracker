package com.pmprogramms.expensetracker.database.state

sealed class UpdateState<out T> {
    data object Loading: UpdateState<Nothing>()
    data class Success<T>(val data: T) : UpdateState<T>()
    data class Error(val error: Throwable) : UpdateState<Nothing>()

}