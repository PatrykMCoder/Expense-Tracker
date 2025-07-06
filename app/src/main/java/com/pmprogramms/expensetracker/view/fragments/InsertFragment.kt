package com.pmprogramms.expensetracker.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.databinding.FragmentHomeBinding
import com.pmprogramms.expensetracker.databinding.FragmentInsertBinding
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertFragment : Fragment() {
    private var _binding: FragmentInsertBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ExpensesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsertBinding.inflate(layoutInflater)
        viewModel =  ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[ExpensesViewModel::class.java]

        binding.button.setOnClickListener {
            val name = binding.name.text.trim().toString()
            val value = binding.value.text.trim().toString().toDouble()
            val category = binding.category.text.trim().toString()
                viewModel.insertExpense(name, value, category) {
//                    findNavController().popBackStack(R.id.homeFragment, false)
            }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}