package com.dalhousie.rapid_crypto_app.persistence.dao


interface AuthResultCallBacks {
    fun onSuccess(message: String)
    fun onError(message: String)
    fun onForgotPassword() {}
    fun onRegistration() {}
    fun onLogin(){}
}