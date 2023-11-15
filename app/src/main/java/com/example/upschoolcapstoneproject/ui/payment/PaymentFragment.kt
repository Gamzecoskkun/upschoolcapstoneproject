package com.example.upschoolcapstoneproject.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.upschoolcapstoneproject.common.viewBinding
import com.example.upschoolcapstoneproject.R
import com.example.upschoolcapstoneproject.common.gone
import com.example.upschoolcapstoneproject.databinding.FragmentPaymentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding(FragmentPaymentBinding::bind)

    private val viewModel by viewModels<PaymentViewModel>()

    private val monthList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private val yearList = listOf(2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, monthList)
        val yearAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, yearList)


        with(binding) {
            btnBuyNow.setOnClickListener {
                viewModel.makePayment(
                    etName.text.toString(),
                    etCardNumber.text.toString(),
                    actMonth.text.toString(),
                    actYear.text.toString(),
                    etCvc.text.toString(),
                    etAddress.text.toString()
                )
            }

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }

            actMonth.setAdapter(monthAdapter)
            actYear.setAdapter(yearAdapter)

            actMonth.setOnItemClickListener { _, _, position, _ ->
                monthList[position]
            }

            actYear.setOnItemClickListener { _, _, position, _ ->
                yearList[position]
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.paymentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                PaymentState.Loading -> progressBar.gone()

                is PaymentState.GoSuccess -> {
                    progressBar.gone()
                    findNavController().navigate(PaymentFragmentDirections.paymentToPaymentSuccess())
                }

                is PaymentState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}