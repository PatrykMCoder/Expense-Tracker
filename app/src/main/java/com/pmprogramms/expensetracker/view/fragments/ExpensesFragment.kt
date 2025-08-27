package com.pmprogramms.expensetracker.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.adapter.ExpensesAdapter
import com.pmprogramms.expensetracker.adapter.listeners.ExpenseClickListener
import com.pmprogramms.expensetracker.database.state.DeleteState
import com.pmprogramms.expensetracker.databinding.FragmentExpensesBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.helper.StringHelper
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class ExpensesFragment : Fragment() {
    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by viewModels()
    private var bottomSheetDialog: BottomSheetDialog? = null

    private val onExpenseClickListener = object : ExpenseClickListener {
        override fun onClick(expenseWithCategory: ExpenseWithCategory) {
            bottomSheetDialog = BottomSheetDialog(requireContext())

            val btmSheet = layoutInflater.inflate(R.layout.bottom_sheet_dialog_expense, null)

            val title = btmSheet.findViewById<TextView>(R.id.title_expense)
            val category = btmSheet.findViewById<TextView>(R.id.category)
            val value = btmSheet.findViewById<TextView>(R.id.value)
            val date = btmSheet.findViewById<TextView>(R.id.date)
            val editButton = btmSheet.findViewById<Button>(R.id.edit_button)
            val deleteButton = btmSheet.findViewById<Button>(R.id.delete_button)

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

            deleteButton.setOnClickListener {
                viewModel.deleteExpense(expenseWithCategory.expense.uid)
            }

            editButton.setOnClickListener {
                bottomSheetDialog?.dismiss()
                bottomSheetDialog = null
                val direction = ExpensesFragmentDirections.actionExpensesFragmentToEditExpenseFragment(expenseWithCategory.expense.uid)
                findNavController().navigate(direction)
            }

            bottomSheetDialog?.setContentView(btmSheet)
            bottomSheetDialog?.show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpensesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expensesAdapter = ExpensesAdapter(false)
        expensesAdapter.setOnClickListener(onExpenseClickListener)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        val decorator = DividerItemDecoration(binding.expensesRecyclerView.context, linearLayoutManager.orientation)

        binding.expensesRecyclerView.apply {
            adapter = expensesAdapter
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(decorator)
        }

        viewModel.deleteState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DeleteState.Error -> { Toast.makeText(context, "Something wrong with delete expense...", Toast.LENGTH_SHORT).show() }
                is DeleteState.Loading -> {}
                is DeleteState.Success<*> -> {
                    bottomSheetDialog?.dismiss()
                    bottomSheetDialog = null
                    Toast.makeText(context, "Expense deleted...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.allExpenses.observe(viewLifecycleOwner) { data ->
            expensesAdapter.setItems(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}