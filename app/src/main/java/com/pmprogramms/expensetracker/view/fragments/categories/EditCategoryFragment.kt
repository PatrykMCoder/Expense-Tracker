package com.pmprogramms.expensetracker.view.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pmprogramms.expensetracker.database.state.UpdateState
import com.pmprogramms.expensetracker.databinding.FragmentEditCategoryBinding
import com.pmprogramms.expensetracker.viewmodel.CategoriesViewModel

class EditCategoryFragment : Fragment() {
    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!

    private val args: EditCategoryFragmentArgs by navArgs()

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategoryById(id = args.id).observe(viewLifecycleOwner) { data ->
            binding.categoryNameEt.setText(data.categoryName)
        }

        viewModel.updateState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UpdateState.Error -> Toast.makeText(context, "Something wrong with update", Toast.LENGTH_SHORT).show()
                is UpdateState.Loading -> {}
                is UpdateState.Success<*> -> findNavController().popBackStack()
            }
        }

        binding.addCategoryButton.setOnClickListener {
            viewModel.updateCategoryNameById(args.id, binding.categoryNameEt.text.toString())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}