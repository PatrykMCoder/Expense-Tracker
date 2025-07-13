package com.pmprogramms.expensetracker.helper

import com.pmprogramms.expensetracker.enums.ExpenseType
import java.util.Currency
import java.util.Locale

class StringHelper {
    companion object {
        fun getCurrentCurrency(): String  = Currency.getInstance(Locale.getDefault()).symbol
        fun getChar(expenseType: ExpenseType): String {
            return when (expenseType) {
                ExpenseType.IN -> "+"
                ExpenseType.OUT -> "-"
            }
        }
    }
}