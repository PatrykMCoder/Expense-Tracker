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
import com.pmprogramms.expensetracker.databinding.FragmentHomeBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.helper.StringHelper
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.utils.DateHelper
import com.pmprogramms.expensetracker.utils.listeners.ExpenseBottomSheetClickListener
import com.pmprogramms.expensetracker.view.dialogs.ExpenseDetailsBottomSheetDialog
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class HomeFragment : Fragment(), ExpenseBottomSheetClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by viewModels()

    private var bottomSheetDialog: ExpenseDetailsBottomSheetDialog? = null

    private val onExpenseClickListener = object : ExpenseClickListener {
        override fun onClick(expenseWithCategory: ExpenseWithCategory) {
            bottomSheetDialog = ExpenseDetailsBottomSheetDialog.instance(expenseWithCategory.expense.uid)
            bottomSheetDialog?.setClicksListener(this@HomeFragment)
            bottomSheetDialog?.show(parentFragmentManager, "expense_details_dialog")
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

        val dayRangeStart = DateHelper.dayRange().first
        val dayRangeEnd = DateHelper.dayRange().second

        val weekRangeStart = DateHelper.weekRange().first
        val weekRangeEnd = DateHelper.weekRange().second

        val monthRangeStart = DateHelper.monthRange().first
        val monthRangeEnd = DateHelper.monthRange().second

        viewModel.getAllExpensesByRange(dayRangeStart, dayRangeEnd).observe(viewLifecycleOwner) { data ->
            binding.overviewCard.todayBalance.text = "${data} ${StringHelper.getCurrentCurrency()}"
        }

        viewModel.getAllExpensesByRange(weekRangeStart, weekRangeEnd).observe(viewLifecycleOwner) { data ->
            binding.overviewCard.weeklyBalance.text = "${data} ${StringHelper.getCurrentCurrency()}"
        }

        viewModel.getAllExpensesByRange(monthRangeStart, monthRangeEnd).observe(viewLifecycleOwner) { data ->
            binding.overviewCard.balanceTextView.text = "${data} ${StringHelper.getCurrentCurrency()}"
        }

        viewModel.deleteState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DeleteState.Error -> { Toast.makeText(context, "Something wrong with delete expense...", Toast.LENGTH_SHORT).show() }
                is DeleteState.Loading -> {}
                is DeleteState.Success<*> -> {
                    bottomSheetDialog?.removeClickListener()
                    bottomSheetDialog?.dismiss()
                    bottomSheetDialog = null
                    Toast.makeText(context, "Expense deleted...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteClick(id: Int) {
        viewModel.deleteExpense(id)
    }

    override fun onEditClick(id: Int) {
        bottomSheetDialog?.removeClickListener()
        bottomSheetDialog?.dismiss()
        bottomSheetDialog = null
        val direction = HomeFragmentDirections.actionHomeFragmentToEditExpenseFragment(id)
        findNavController().navigate(direction)
    }
}