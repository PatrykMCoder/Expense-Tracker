package com.pmprogramms.expensetracker.utils.listeners

interface ExpenseBottomSheetClickListener {
    fun onDeleteClick(id: Int)
    fun onEditClick(id: Int)
}