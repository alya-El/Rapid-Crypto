package com.dalhousie.rapid_crypto_app.models

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.BaseObservable

class User (private var email: String, private var password: String) : BaseObservable(){

    fun isDataValid():Int
    {
        if(TextUtils.isEmpty(getEmail()))
            return 0
        //android Regex to check the email address Validation
        else if(!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches())
            return 1
        else if (TextUtils.isEmpty(getPassword()))
            return 2
        else
            return -1
    }

    fun getEmail() : String {
        return email
    }
    fun getPassword() : String {
        return password
    }
    fun setEmail(email: String){
        this.email = email
    }
    fun setPassword(password: String) {
        this.password = password
    }
}