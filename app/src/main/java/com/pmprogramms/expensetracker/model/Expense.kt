package com.pmprogramms.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pmprogramms.expensetracker.enums.ExpenseType

@Entity(tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["category_id"]
    )])
data class Expense(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "value") val value: Double?,
    @ColumnInfo(name = "category_id") val categoryID: Int,
    @ColumnInfo(name = "type") val expenseType: ExpenseType,
//    @ColumnInfo(name = "createdAt") val createdAt: Long
)
