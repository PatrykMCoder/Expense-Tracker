package com.pmprogramms.expensetracker.helper

import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.Category

data class ExpenseFilter(
    var category: Category? = null,
    var tsFrom: Long? = null,
    var tsTo: Long? = null,
    var valueFrom: Double? = null,
    var valueTo: Double? = null,
    var type: ExpenseType? = null
) {
}
