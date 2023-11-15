package com.example.upschoolcapstoneproject.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.upschoolcapstoneproject.common.viewBinding
import com.example.upschoolcapstoneproject.R
import com.example.upschoolcapstoneproject.common.gone
import com.example.upschoolcapstoneproject.common.visible
import com.example.upschoolcapstoneproject.databinding.FragmentCartBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val binding by viewBinding(FragmentCartBinding::bind)

    private val viewModel by viewModels<CartViewModel>()

    private val cartAdapter = CartAdapter(
        onProductClick = ::onProductClick,
        onDeleteClick = ::onDeleteClick
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCartProduct()

        with(binding) {

            rvCart.adapter = cartAdapter

            ivClear.setOnClickListener {
                viewModel.clearCart()
            }
            btnBuy.setOnClickListener {
                findNavController().navigate(CartFragmentDirections.cartToPayment())
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {

        viewModel.cartState.observe(viewLifecycleOwner) {
            when (it) {
                CartState.Loading -> {
                    progressBar.visible()
                    tvTotalAmount.gone()
                }

                is CartState.SuccessState -> {
                    progressBar.gone()
                    cartAdapter.submitList(it.products)
                    tvTotalAmount.visible()
                }

                is CartState.EmptyScreen -> {
                    progressBar.gone()
                    rvCart.gone()
                    tvEmpty.text = it.failMessage
                    tvEmpty.visible()
                    ivEmpty.visible()
                    tvTotalAmount.gone()
                    textView8.gone()
                    btnBuy.gone()
                }

                is CartState.ShowPopUp -> {
                    progressBar.gone()
                    tvTotalAmount.gone()
                    Snackbar.make(requireView(), it.errorMessage, 1000).show()
                }
            }
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) { amount ->
            tvTotalAmount.text = String.format(" %.0f ₺", amount)
        }
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(CartFragmentDirections.cartToDetail(id))
    }

    private fun onDeleteClick(productId: Int) {
        viewModel.deleteFromCart(productId)
    }
}


