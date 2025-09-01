package com.pmprogramms.expensetracker.view.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.databinding.BottomSheetDialogExpenseBinding
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.helper.StringHelper
import com.pmprogramms.expensetracker.utils.listeners.ExpenseBottomSheetClickListener
import com.pmprogramms.expensetracker.viewmodel.ExpensesViewModel

class ExpenseDetailsBottomSheetDialog : BottomSheetDialogFragment() {

    private var expenseBottomSheetClickListener: ExpenseBottomSheetClickListener? = null
    private var _binding: BottomSheetDialogExpenseBinding? = null
    private val binding get() = _binding!!

    private val expensesViewModel: ExpensesViewModel by viewModels()

    companion object {
        fun instance(id: Int): ExpenseDetailsBottomSheetDialog {
            val expenseDetailsBottomSheetDialog = ExpenseDetailsBottomSheetDialog()

            val arguments = Bundle()
            arguments.putInt("expenseID", id)

            expenseDetailsBottomSheetDialog.arguments = arguments

            return expenseDetailsBottomSheetDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogExpenseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("expenseID")
        id?.let {
            expensesViewModel.getExpenseById(id).observe(viewLifecycleOwner) { data ->
                val expense = data.expense
                val category = data.category
                binding.title.text = expense.name
                binding.category.text = "Category: ${category?.categoryName}"
                binding.date.text = "no date"
                binding.value.text = "${StringHelper.getChar(expense.expenseType)}${expense.value} ${StringHelper.getCurrentCurrency()}"

                when (expense.expenseType) {
                    ExpenseType.IN -> {
                        binding.value.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    }
                    ExpenseType.OUT -> {
                        binding.value.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                }

                binding.deleteButton.setOnClickListener {
                    expenseBottomSheetClickListener?.onDeleteClick(expense.uid)
                }

                binding.editButton.setOnClickListener {
                    expenseBottomSheetClickListener?.onEditClick(expense.uid)
                }
            }
        }
    }

    fun setClicksListener(expenseBottomSheetClickListener: ExpenseBottomSheetClickListener) {
        this.expenseBottomSheetClickListener = expenseBottomSheetClickListener
    }

    fun removeClickListener() {
        this.expenseBottomSheetClickListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}