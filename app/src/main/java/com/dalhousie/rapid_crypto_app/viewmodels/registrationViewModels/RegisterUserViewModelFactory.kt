package com.dalhousie.rapid_crypto_app.viewmodels.registrationViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dalhousie.rapid_crypto_app.fragments.registration.RegisterUserFragment

//class to instantiate and return RegisterUserViewModel
class RegisterUserViewModelFactory(private val listener: RegisterUserFragment): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterUserViewModel(listener) as T
    }
}