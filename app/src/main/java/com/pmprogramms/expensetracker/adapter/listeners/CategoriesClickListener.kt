package com.pmprogramms.expensetracker.adapter.listeners

interface CategoriesClickListener {
    fun onDeleteClick(categoryID: Int)
    fun onEditClick(categoryID: Int)
}
