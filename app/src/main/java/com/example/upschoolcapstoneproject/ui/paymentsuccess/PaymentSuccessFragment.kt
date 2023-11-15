package com.example.upschoolcapstoneproject.ui.paymentsuccess

import android.os.Bundle
import android.system.Os.bind
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.upschoolcapstoneproject.common.viewBinding
import com.example.upschoolcapstoneproject.R
import com.example.upschoolcapstoneproject.databinding.FragmentDetailBinding
import com.example.upschoolcapstoneproject.databinding.FragmentPaymentSucecessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentSuccessFragment : Fragment(R.layout.fragment_payment_sucecess) {

    private val binding by viewBinding(FragmentPaymentSucecessBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnBackToHome.setOnClickListener {
                findNavController().navigate(PaymentSuccessFragmentDirections.paymentSuccessToHome())
            }
        }
    }
}