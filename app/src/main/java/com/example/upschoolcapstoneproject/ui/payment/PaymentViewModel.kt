package com.example.upschoolcapstoneproject.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upschoolcapstoneproject.common.Resource
import com.example.upschoolcapstoneproject.data.repository.AuthRepository
import com.example.upschoolcapstoneproject.data.repository.ProductRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val productRepository: ProductRepository, private val authRepository: AuthRepository
) : ViewModel() {

    private var _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> get() = _paymentState

    fun makePayment(
        name: String, cardNumber: String,month:String,year:String, cvc: String, address: String
    ) = viewModelScope.launch {
        _paymentState.value = PaymentState.Loading

        val errorMessage = when {
            name.isEmpty() -> "Name cannot be left blank!"
            cardNumber.isEmpty() || cardNumber.length != 16 -> "Invalid card number!"
            month.isEmpty() -> "Month cannot be left blank!"
            year.isEmpty() -> "Year cannot be left blank!"
            cvc.isEmpty() || cvc.length < 3 -> "Invalid CVC!"
            address.isEmpty() -> "Address cannot be left blank!"
            else -> null
        }

        if (errorMessage != null) {
            _paymentState.value = PaymentState.ShowPopUp(errorMessage)
        } else {
            clearCart()
        }
    }

    fun clearCart() = viewModelScope.launch {

        _paymentState.value =
            when (val result = productRepository.clearCart(authRepository.getUserId())) {
                is Resource.Success -> PaymentState.GoSuccess
                is Resource.Fail -> PaymentState.ShowPopUp(result.failMessage)
                is Resource.Error -> PaymentState.ShowPopUp(result.errorMessage)
            }
    }
}

sealed interface PaymentState {
    object Loading : PaymentState
    object GoSuccess : PaymentState
    data class ShowPopUp(val errorMessage: String) : PaymentState
}

