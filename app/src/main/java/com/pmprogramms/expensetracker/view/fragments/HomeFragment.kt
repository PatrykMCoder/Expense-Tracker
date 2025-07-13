package com.pmprogramms.expensetracker.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.adapter.ExpensesAdapter
import com.pmprogramms.expensetracker.adapter.listeners.ExpenseClickListener
import com.pmprogramms.expensetracker.databinding.FragmentHomeBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel
import kotlin.reflect.typeOf

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by viewModels()

    private val onExpenseClickListener = object : ExpenseClickListener {
        override fun onClick(expenseWithCategory: ExpenseWithCategory) {
            // open information
            Log.d("TAG", "onClick: $expenseWithCategory")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expensesAdapter = ExpensesAdapter()
        expensesAdapter.setOnClickListener(onExpenseClickListener)

        binding.expensesRecyclerView.apply {
            adapter = expensesAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.insertFragment)
        }

        binding.button.setOnLongClickListener {
            findNavController().navigate(R.id.addCategoryFragment)
            return@setOnLongClickListener true
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