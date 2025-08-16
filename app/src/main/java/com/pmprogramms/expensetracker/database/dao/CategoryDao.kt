package com.pmprogramms.expensetracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pmprogramms.expensetracker.model.Category

@Dao
interface CategoryDao {
    @Query("select * from categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("DELETE from categories where category_id = :categoryID")
    suspend fun deleteCategoryByID(categoryID: Int)
}