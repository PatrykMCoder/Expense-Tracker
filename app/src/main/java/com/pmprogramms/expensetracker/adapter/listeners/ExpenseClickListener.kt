package com.pmprogramms.expensetracker.adapter.listeners

import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory

interface ExpenseClickListener {
    fun onClick(expenseWithCategory: ExpenseWithCategory)
}
