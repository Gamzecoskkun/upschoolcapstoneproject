package com.example.upschoolcapstoneproject.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upschoolcapstoneproject.common.Resource
import com.example.upschoolcapstoneproject.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> get() = _signUpState

    fun signUp(email: String, password: String) = viewModelScope.launch {
        if (checkFields(email, password)) {
            _signUpState.value = SignUpState.Loading

            _signUpState.value = when (val result = authRepository.signUp(email, password)) {
                is Resource.Success -> SignUpState.GoToHome
                is Resource.Fail -> SignUpState.ShowPopUp(result.failMessage)
                is Resource.Error -> SignUpState.ShowPopUp(result.errorMessage)
            }
        }
    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _signUpState.value = SignUpState.ShowPopUp("Email cannot be left blank!")
                false
            }

            password.isEmpty() -> {
                _signUpState.value = SignUpState.ShowPopUp("Password cannot be left blank!")
                false
            }

            password.length < 6 -> {
                _signUpState.value =
                    SignUpState.ShowPopUp("Password should be 6 characters at least!")
                false
            }

            else -> true
        }
    }
}

sealed interface SignUpState {
    object Loading : SignUpState
    object GoToHome : SignUpState
    data class ShowPopUp(val errorMessage: String) : SignUpState
}



