package com.pmprogramms.expensetracker.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.adapter.ExpensesAdapter
import com.pmprogramms.expensetracker.adapter.listeners.ExpenseClickListener
import com.pmprogramms.expensetracker.databinding.FragmentHomeBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.helper.StringHelper
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by viewModels()

    private val onExpenseClickListener = object : ExpenseClickListener {
        override fun onClick(expenseWithCategory: ExpenseWithCategory) {
            val bottomSheetDialog = BottomSheetDialog(requireContext())

            val btmSheet = layoutInflater.inflate(R.layout.bottom_sheet_dialog_expense, null)

            val title = btmSheet.findViewById<TextView>(R.id.title_expense)
            val category = btmSheet.findViewById<TextView>(R.id.category)
            val value = btmSheet.findViewById<TextView>(R.id.value)
            val date = btmSheet.findViewById<TextView>(R.id.date)

            title.text = "Title: ${expenseWithCategory.expense.name}"
            category.text = "Category: ${expenseWithCategory.category?.categoryName}"
            date.text = "no date"
            value.text = "${StringHelper.getChar(expenseWithCategory.expense.expenseType)}${expenseWithCategory.expense.value} ${StringHelper.getCurrentCurrency()}"

            when (expenseWithCategory.expense.expenseType) {
                ExpenseType.IN -> {
                    value.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
                ExpenseType.OUT -> {
                    value.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }

            }

            bottomSheetDialog.setContentView(btmSheet)
            bottomSheetDialog.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expensesAdapter = ExpensesAdapter(true)
        expensesAdapter.setOnClickListener(onExpenseClickListener)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        val decorator = DividerItemDecoration(binding.expensesRecyclerView.context, linearLayoutManager.orientation)

        binding.expensesRecyclerView.apply {
            adapter = expensesAdapter
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(decorator)
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.insertFragment)
        }

        binding.showCategoriesButton.setOnClickListener {
            findNavController().navigate(R.id.categoriesFragment)
        }

        binding.showAllButton.setOnClickListener {
            findNavController().navigate(R.id.expensesFragment)
        }

        viewModel.allExpenses.observe(viewLifecycleOwner) { data ->
            expensesAdapter.setItems(data)
        }

        viewModel.getCurrentMonthBalance(0, 0).observe(viewLifecycleOwner) { data ->
            binding.balanceTextView.text = "${data} ${StringHelper.getCurrentCurrency()}"
        }

        viewModel.getWeekBalance(0, 0).observe(viewLifecycleOwner) { data ->
            binding.weeklyBalance.text = "${data} ${StringHelper.getCurrentCurrency()}"
        }

        viewModel.getTodayBalance(0, 0).observe(viewLifecycleOwner) { data ->
            binding.todayBalance.text = "${data} ${StringHelper.getCurrentCurrency()}"
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}