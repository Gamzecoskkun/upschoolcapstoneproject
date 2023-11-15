package com.example.upschoolcapstoneproject.ui.detail

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
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState> get() = _detailState

    private var detailData:ProductUI?=null

    fun getDetailProducts(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading

        _detailState.value = when (val result = productRepository.getProductDetail(id)) {
            is Resource.Success -> DetailState.SuccessState(result.data)
            is Resource.Fail -> DetailState.EmptyScreen(result.failMessage)
            is Resource.Error -> DetailState.ShowPopUp(result.errorMessage)
        }
    }

    fun addToCart(productId: Int) = viewModelScope.launch {
        productRepository.addToCart(authRepository.getUserId(), productId)
    }

    fun setFavorites(id:Int){
        viewModelScope.launch {
            detailData?.let {
                if (it.isFav){
                    productRepository.deleteFromFavorites(it)
                }else{
                    productRepository.addToFavorites(it)
                }
                getDetailProducts(id)
            }
        }
    }
}

sealed interface DetailState {
    object Loading : DetailState
    data class SuccessState(val product: ProductUI) : DetailState
    data class ShowPopUp(val errorMessage: String) : DetailState
    data class EmptyScreen(val failMessage: String) : DetailState
}


