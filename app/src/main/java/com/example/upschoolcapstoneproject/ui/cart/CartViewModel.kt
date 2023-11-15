package com.example.upschoolcapstoneproject.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upschoolcapstoneproject.common.Resource
import com.example.upschoolcapstoneproject.data.model.response.ProductUI
import com.example.upschoolcapstoneproject.data.repository.AuthRepository
import com.example.upschoolcapstoneproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository, private val authRepository: AuthRepository
) : ViewModel() {

    private val _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState> get() = _cartState

    private val _totalAmount = MutableLiveData(0.0)
    val totalAmount: LiveData<Double> = _totalAmount

    fun getCartProduct() = viewModelScope.launch {

        _cartState.value = CartState.Loading

        _cartState.value = when (val result = productRepository.getCartProducts(authRepository.getUserId())) {
            is Resource.Success -> {
                _totalAmount.value = result.data.sumOf { it ->
                    if (it.saleState) {
                        it.salePrice
                    } else it.price
                }
                CartState.SuccessState(result.data)
            }

            is Resource.Fail -> CartState.EmptyScreen(result.failMessage)
            is Resource.Error -> CartState.ShowPopUp(result.errorMessage)
        }
    }

    fun deleteFromCart(productId: Int) = viewModelScope.launch {
        _cartState.value = CartState.Loading

        _cartState.value = when (val result = productRepository.deleteFromCart(authRepository.getUserId(), productId)) {
            is Resource.Success -> {
                getCartProduct()
                resetTotalAmount()
                CartState.ShowPopUp(result.data.message.orEmpty())
            }

            is Resource.Fail -> CartState.ShowPopUp(result.failMessage)
            is Resource.Error -> CartState.ShowPopUp(result.errorMessage)
        }
    }

    fun clearCart() = viewModelScope.launch {
        _cartState.value = CartState.Loading

        _cartState.value =
            when (val result = productRepository.clearCart(authRepository.getUserId())) {
                is Resource.Success -> {
                    resetTotalAmount()
                    CartState.ShowPopUp(result.data.message.orEmpty())
                }

                is Resource.Fail -> CartState.ShowPopUp(result.failMessage)
                is Resource.Error -> CartState.ShowPopUp(result.errorMessage)
            }
        getCartProduct()

    }


    private fun resetTotalAmount() {
        _totalAmount.value = 0.0
    }
}

sealed interface CartState {
    object Loading : CartState
    data class SuccessState(val products: List<ProductUI>) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class ShowPopUp(val errorMessage: String) : CartState
}



