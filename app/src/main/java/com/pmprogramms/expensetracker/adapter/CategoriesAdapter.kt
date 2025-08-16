package com.pmprogramms.expensetracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pmprogramms.expensetracker.adapter.listeners.CategoriesClickListener
import com.pmprogramms.expensetracker.databinding.CategoryItemBinding
import com.pmprogramms.expensetracker.model.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private lateinit var categoriesClickListener: CategoriesClickListener
    private var items: ArrayList<Category> = ArrayList()

    inner class CategoriesViewHolder(private val categoryItemBinding: CategoryItemBinding): RecyclerView.ViewHolder(categoryItemBinding.root) {
        fun bind(category: Category) {
            categoryItemBinding.categoryName.text = category.categoryName

            categoryItemBinding.deleteButton.setOnClickListener {
                categoriesClickListener.onDeleteClick(category.categoryID!!)
            }
        }
    }

    fun setItems(items: List<Category>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
        binding.root.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return CategoriesViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setOnCategoriesClickListener(categoriesClickListener: CategoriesClickListener) {
        this.categoriesClickListener = categoriesClickListener
    }
}