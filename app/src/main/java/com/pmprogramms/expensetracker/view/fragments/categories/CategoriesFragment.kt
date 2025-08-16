package com.pmprogramms.expensetracker.view.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.adapter.CategoriesAdapter
import com.pmprogramms.expensetracker.adapter.listeners.CategoriesClickListener
import com.pmprogramms.expensetracker.database.state.DeleteState
import com.pmprogramms.expensetracker.databinding.FragmentCategoriesBinding
import com.pmprogramms.expensetracker.viewmodel.CategoriesViewModel

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoriesViewModel by viewModels()

    private val categoriesClickListener = object : CategoriesClickListener {
        override fun onDeleteClick(categoryID: Int) {
            viewModel.deleteCategory(categoryID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesAdapter = CategoriesAdapter()
        categoriesAdapter.setOnCategoriesClickListener(categoriesClickListener)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        val decorator = DividerItemDecoration(binding.categoriesRv.context, linearLayoutManager.orientation)

        binding.categoriesRv.apply {
            adapter = categoriesAdapter
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(decorator)
        }

        binding.addCategoryButton.setOnClickListener {
            findNavController().navigate(R.id.addCategoryFragment)
        }

        viewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setItems(categories)
        }

        viewModel.deleteState.observe(viewLifecycleOwner) { result ->
            when(result) {
                is DeleteState.Error -> Toast.makeText(context, "Category can't be deleted!", Toast.LENGTH_SHORT).show()
                is DeleteState.Loading -> Toast.makeText(context, "Removing", Toast.LENGTH_SHORT).show()
                is DeleteState.Success<*> ->  Toast.makeText(context, "Category deleted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}