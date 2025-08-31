package com.pmprogramms.expensetracker.utils

import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory

class MathHelper {
    companion object {
        fun sumExpenses(data: List<ExpenseWithCategory>) = data.sumOf {
            if (it.expense.expenseType == ExpenseType.IN) {
                it.expense.value!!
            } else {
                it.expense.value!! * -1
            }
        }
    }
}