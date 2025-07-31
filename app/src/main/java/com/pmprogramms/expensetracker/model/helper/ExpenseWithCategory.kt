package com.pmprogramms.expensetracker.model.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.pmprogramms.expensetracker.model.Category
import com.pmprogramms.expensetracker.model.Expense

data class ExpenseWithCategory(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val category: Category?
)
