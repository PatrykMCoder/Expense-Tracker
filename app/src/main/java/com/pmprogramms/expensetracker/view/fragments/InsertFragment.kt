package com.pmprogramms.expensetracker.view.fragments

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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

        val spinnerAdapter = ArrayAdapter<Category>(
            requireContext(),
            R.layout.simple_spinner_item,
            mutableListOf()
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }

        binding.categorySpinner.adapter = spinnerAdapter

        categoriesViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            spinnerAdapter.clear()
            spinnerAdapter.addAll(categories)
        }

        binding.button.setOnClickListener {
            val name = binding.name.text.trim().toString()
            val value = binding.value.text.trim().toString().toDouble()
            val category = binding.categorySpinner.selectedItem as? Category
                if (category != null) {
                    expensesViewModel.insertExpense(name, value, category.categoryID, ExpenseType.OUT) {
                    }
                }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}