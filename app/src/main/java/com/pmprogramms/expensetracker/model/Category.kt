package com.pmprogramms.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("category_id")
    val categoryID: Int,
    @ColumnInfo(name = "category_name") val categoryName: String
) {
    override fun toString(): String = categoryName
}
