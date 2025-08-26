package com.pmprogramms.expensetracker.view.fragments

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pmprogramms.expensetracker.database.state.UpdateState
import com.pmprogramms.expensetracker.databinding.FragmentEditExpenseBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.Category
import com.pmprogramms.expensetracker.viewmodel.CategoriesViewModel
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class EditExpenseFragment : Fragment() {
    private var _binding: FragmentEditExpenseBinding? = null
    private val binding get() = _binding!!

    private val expensesViewModel: ExpensesViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()

    private val args: EditExpenseFragmentArgs by navArgs()

    private val categories: ArrayList<Category?> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditExpenseBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryArrayAdapter = ArrayAdapter<Category>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val expenseTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            ExpenseType.entries.toTypedArray()
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }


        binding.categorySpinner.adapter = categoryArrayAdapter
        binding.expenseTypeSpinner.adapter = expenseTypeAdapter

        binding.dividerTextCategory.dividerText.text = "Category"
        binding.dividerTextTypeExpense.dividerText.text = "Type of expense"

        categoriesViewModel.allCategories.observe(viewLifecycleOwner) {
            categoryArrayAdapter.clear()
            val emptyCategory = Category(null, "No category")
            categories.add(emptyCategory)
            categories.addAll(it)
            categoryArrayAdapter.addAll(categories)
        }

        expensesViewModel.getExpenseById(args.expenseID).observe(viewLifecycleOwner) {
            val expense = it.expense
            val category = it.category
            binding.expenseName.setText(expense.name)
            binding.expenseValue.setText(expense.value.toString())
            binding.categorySpinner.setSelection(categories.indexOf(category))
            binding.expenseTypeSpinner.setSelection(ExpenseType.entries.indexOf(expense.expenseType))
        }

        expensesViewModel.updateState.observe(viewLifecycleOwner) { result ->
            when(result) {
                is UpdateState.Error -> { Toast.makeText(context, "Something wrong with update expense...", Toast.LENGTH_SHORT).show() }
                is UpdateState.Loading ->  {}
                is UpdateState.Success<*> -> {
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }

        binding.button.setOnClickListener {
            val newName = binding.expenseName.text.toString()
            val newValue = binding.expenseValue.text.toString().toDouble()
            val category = binding.categorySpinner.selectedItem as? Category
            val newCategoryID = if (category?.categoryID != null) {
                category.categoryID
            } else {
                null
            }
            val expenseType = binding.expenseTypeSpinner.selectedItem as ExpenseType

            binding.categorySpinner.setSelection(categories.indexOf(category))
            expensesViewModel.updateExpense(args.expenseID, newName, newValue, newCategoryID, expenseType)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}