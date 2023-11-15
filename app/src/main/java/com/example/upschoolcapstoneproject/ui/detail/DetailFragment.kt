package com.example.upschoolcapstoneproject.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.upschoolcapstoneproject.R
import com.example.upschoolcapstoneproject.common.gone
import com.example.upschoolcapstoneproject.common.strike
import com.example.upschoolcapstoneproject.common.viewBinding
import com.example.upschoolcapstoneproject.common.visible
import com.example.upschoolcapstoneproject.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()

    private val authId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    private val imageAdapter = DetailAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDetailProducts(args.id)

        observeData()

        with(binding) {
            btnAddToCart.setOnClickListener {
                viewModel.addToCart(args.id)
            }

            ivFavorite.setOnClickListener {
                viewModel.setFavorites(args.id)
            }
            rvDetailImage.adapter = imageAdapter
        }
    }

    private fun observeData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailState.Loading -> progressBar.visible()

                is DetailState.SuccessState -> {
                    progressBar.gone()
                    val imageList = listOf(
                        state.product.imageOne,
                        state.product.imageTwo,
                        state.product.imageThree
                    )
                    imageAdapter.updateList(imageList)
                    tvTitle.text = state.product.title
                    tvCategory.text = state.product.category
                    tvDescription.text = state.product.description
                    tvRate.text = "${state.product.rate}"
                    ratingBar.rating = state.product.rate.toFloat()


                    if (state.product.saleState) {
                        tvPrice.text = "${state.product.price} ₺"
                        tvSalePrice.text = "${state.product.salePrice}₺"
                        tvPrice.setTextColor(Color.parseColor("#FF0000"))
                        tvPrice.strike = true

                    } else {
                        tvPrice.text = "${state.product.price}₺"
                        tvSalePrice.gone()
                    }

                    ivFavorite.setBackgroundResource(
                        if (state.product.isFav) R.drawable.ic_fav_selected
                        else R.drawable.ic_fav_unselected
                    )

                    ivBack.setOnClickListener {
                        findNavController().navigateUp()
                    }
                }

                is DetailState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is DetailState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}


