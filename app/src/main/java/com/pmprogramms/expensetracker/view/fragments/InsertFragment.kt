package com.pmprogramms.expensetracker.view.fragments

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pmprogramms.expensetracker.databinding.FragmentInsertBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.Category
import com.pmprogramms.expensetracker.viewmodel.CategoriesViewModel
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class InsertFragment : Fragment() {
    private var _binding: FragmentInsertBinding? = null
    private val binding get() = _binding!!

    private val expensesViewModel: ExpensesViewModel by viewModels()
    private val categoriesViewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsertBinding.inflate(layoutInflater)

        val categoryArrayAdapter = ArrayAdapter<Category>(
            requireContext(),
            R.layout.simple_spinner_item,
            mutableListOf()
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
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
            val categories = ArrayList<Category?>()
            val emptyCategory = Category(null, "No category")
            categories.add(emptyCategory)
            categories.addAll(it)
            categoryArrayAdapter.addAll(categories)
        }

        binding.button.setOnClickListener {
            val name = binding.expenseName.text.trim().toString()
            val value = binding.expenseValue.text.trim().toString().toDouble()
            val category = binding.categorySpinner.selectedItem as? Category
            val categoryID = if (category?.categoryID != null) {
                category.categoryID
            } else {
                null
            }
            val expenseType = binding.expenseTypeSpinner.selectedItem as ExpenseType

            expensesViewModel.insertExpense(name, value, categoryID, expenseType) {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}