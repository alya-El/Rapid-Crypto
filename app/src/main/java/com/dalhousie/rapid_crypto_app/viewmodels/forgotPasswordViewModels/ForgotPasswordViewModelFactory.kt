package com.dalhousie.rapid_crypto_app.viewmodels.forgotPasswordViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dalhousie.rapid_crypto_app.persistence.dao.AuthResultCallBacks

//class to instantiate and return ForgotPasswordViewModel
class ForgotPasswordViewModelFactory (private val listener: AuthResultCallBacks): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForgotPasswordViewModel(listener) as T
    }
}