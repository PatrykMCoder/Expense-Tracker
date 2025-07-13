package com.pmprogramms.expensetracker.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pmprogramms.expensetracker.adapter.listeners.ExpenseClickListener
import com.pmprogramms.expensetracker.databinding.ExpenseItemBinding
import com.pmprogramms.expensetracker.helper.StringHelper
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import kotlin.math.exp

class ExpensesAdapter: RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder>() {
    private lateinit var onClickListener: ExpenseClickListener
    private val items = ArrayList<ExpenseWithCategory>()

    inner class ExpensesViewHolder(private val binding: ExpenseItemBinding): ViewHolder(binding.root) {
        fun bind(expenseWithCategory: ExpenseWithCategory, onClickListener: ExpenseClickListener) {
            val expense = expenseWithCategory.expense
            val category = expenseWithCategory.category

            binding.name.text = expense.name
            binding.value.text = "${StringHelper.getChar(expense.expenseType)}${expense.value} ${StringHelper.getCurrentCurrency()}"
            binding.category.text = category.categoryName

            binding.root.setOnClickListener {
                onClickListener.onClick(expenseWithCategory)
            }
        }
    }

    fun setItems(items: List<ExpenseWithCategory>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: ExpenseClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val binding = ExpenseItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
        binding.root.layoutParams = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return ExpensesViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        holder.bind(items[position], onClickListener)
    }
}