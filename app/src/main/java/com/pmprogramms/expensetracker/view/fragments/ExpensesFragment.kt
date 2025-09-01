package com.pmprogramms.expensetracker.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationBarView
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.adapter.ExpensesAdapter
import com.pmprogramms.expensetracker.adapter.listeners.ExpenseClickListener
import com.pmprogramms.expensetracker.database.state.DeleteState
import com.pmprogramms.expensetracker.databinding.FragmentExpensesBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.helper.ExpenseFilter
import com.pmprogramms.expensetracker.helper.StringHelper
import com.pmprogramms.expensetracker.model.Category
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.utils.DateHelper
import com.pmprogramms.expensetracker.utils.listeners.ExpenseBottomSheetClickListener
import com.pmprogramms.expensetracker.view.dialogs.ExpenseDetailsBottomSheetDialog
import com.pmprogramms.expensetracker.viewmodel.CategoriesViewModel
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class ExpensesFragment : Fragment(), ExpenseBottomSheetClickListener {
    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    private val expensesViewModel: ExpensesViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private var bottomSheetDialog: ExpenseDetailsBottomSheetDialog? = null

    private val filter = ExpenseFilter()

    private val onExpenseClickListener = object : ExpenseClickListener {
        override fun onClick(expenseWithCategory: ExpenseWithCategory) {
            bottomSheetDialog = ExpenseDetailsBottomSheetDialog.instance(expenseWithCategory.expense.uid)

            bottomSheetDialog?.setClicksListener(this@ExpensesFragment)

            bottomSheetDialog?.show(parentFragmentManager, "expense_details_dialog")
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

        val categoryArrayAdapter = ArrayAdapter<Category>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val expenseTypeFilters = ArrayList<String>()

        expenseTypeFilters.add("ALL")
        expenseTypeFilters.addAll(ExpenseType.entries.map { it.name })

        val expenseTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            expenseTypeFilters
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        expensesViewModel.setFilter(filter)

        binding.datePickerFrom.paintFlags = android.graphics.Paint.UNDERLINE_TEXT_FLAG
        binding.datePickerTo.paintFlags = android.graphics.Paint.UNDERLINE_TEXT_FLAG

        binding.datePickerFrom.setOnClickListener {
            val dialog = DatePickerDialog(requireContext())
            dialog.setOnDateSetListener { datePicker, year, month, day ->
                val mills = DateHelper.getMillsFromDate(day, month, year)
                binding.datePickerFrom.text = "$day.$month.$year"
                filter.tsFrom = mills

                expensesViewModel.setFilter(filter)
            }

            dialog.show()
        }

        binding.datePickerTo.setOnClickListener {
            val dialog = DatePickerDialog(requireContext())
            dialog.setOnDateSetListener { datePicker, year, month, day ->
                val mills = DateHelper.getMillsFromDate(day, month, year)
                binding.datePickerTo.text = "$day.$month.$year"
                filter.tsTo = mills

                expensesViewModel.setFilter(filter)
            }

            dialog.show()
        }

        binding.expenseTypeSpinner.adapter = expenseTypeAdapter
        binding.categoriesSpinner.adapter = categoryArrayAdapter

        binding.expenseTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                if (p2 != 0) {
                    filter.type = ExpenseType.entries[p2 - 1]
                } else {
                    filter.type = null
                }

                expensesViewModel.setFilter(filter)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.categoriesSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                if (p2 != 0) {
                    filter.category = categoryArrayAdapter.getItem(p2)
                } else {
                    filter.category = null
                }
                expensesViewModel.setFilter(filter)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.valuePickerTo.addTextChangedListener {
            val textValue =  binding.valuePickerTo.text.toString()
            if (textValue.isEmpty()) {
                filter.valueTo = null
            } else {
                filter.valueTo = textValue.toDouble()
            }
            expensesViewModel.setFilter(filter)
        }

        binding.valuePickerFrom.addTextChangedListener {
            val textValue =  binding.valuePickerFrom.text.toString()
            if (textValue.isEmpty()) {
                filter.valueFrom = null
            } else {
                filter.valueFrom = textValue.toDouble()
            }
            expensesViewModel.setFilter(filter)
        }

        binding.clearDatePickerToButton.setOnClickListener {
            binding.datePickerTo.text = "Date to..."
            filter.tsTo = null

            expensesViewModel.setFilter(filter)
        }
        binding.clearDatePickerFromButton.setOnClickListener {
            binding.datePickerFrom.text = "Date from..."
            filter.tsFrom = null

            expensesViewModel.setFilter(filter)
        }

        categoriesViewModel.allCategories.observe(viewLifecycleOwner) {
            categoryArrayAdapter.clear()
            val categories = ArrayList<Category?>()

            val emptyCategory = Category(null, "No filter")

            categories.add(emptyCategory)
            categories.addAll(it)
            categoryArrayAdapter.addAll(categories)
        }

        binding.expensesRecyclerView.apply {
            adapter = expensesAdapter
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(decorator)
        }

        expensesViewModel.deleteState.observe(viewLifecycleOwner) { result ->
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

        expensesViewModel.allExpenses.observe(viewLifecycleOwner) { data ->
            expensesAdapter.setItems(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteClick(id: Int) {
        expensesViewModel.deleteExpense(id)
    }

    override fun onEditClick(id: Int) {
        bottomSheetDialog?.removeClickListener()
        bottomSheetDialog?.dismiss()
        bottomSheetDialog = null
        val direction = ExpensesFragmentDirections.actionExpensesFragmentToEditExpenseFragment(id)
        findNavController().navigate(direction)
    }
}