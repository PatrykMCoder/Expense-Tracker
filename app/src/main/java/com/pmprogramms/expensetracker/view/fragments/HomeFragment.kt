package com.pmprogramms.expensetracker.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.databinding.FragmentHomeBinding
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ExpensesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[ExpensesViewModel::class.java]

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.insertFragment)
        }

        viewModel.allExpenses.observe(viewLifecycleOwner) { data ->
            Log.d("TAG", "onViewCreated: ${data.joinToString(", ")}")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}