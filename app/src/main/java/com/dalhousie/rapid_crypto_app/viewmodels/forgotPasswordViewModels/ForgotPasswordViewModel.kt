package com.dalhousie.rapid_crypto_app.viewmodels.forgotPasswordViewModels

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModel
import com.dalhousie.rapid_crypto_app.models.User
import com.dalhousie.rapid_crypto_app.persistence.dao.AuthResultCallBacks
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel(private val listener: AuthResultCallBacks): ViewModel() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var confirmPassword : String

    private val user : User = User("","")

    //TextView listener bound to fragment_forgot_password.xml
    val emailTextWatcher: TextWatcher
        get() = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Does nothing intentionally
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Does nothing intentionally
            }

            override fun afterTextChanged(p0: Editable?) {
                user.setEmail(p0.toString().replace("\\s".toRegex(), ""))
            }
        }

    fun onSendEmailClicked(v: View){
        var loginCode:Int = user.isDataValid()

        firebaseAuth = FirebaseAuth.getInstance()

        when (loginCode) {
            //validates email user inputs
            1 -> listener.onError("Please enter a correct email")
            0 -> listener.onError("Please enter your email")
            else -> {
                firebaseAuth.sendPasswordResetEmail(user.getEmail())
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            //sends email to email given to reset password, done through Firebase
                            listener.onSuccess("Email sent successfully!")
                        }
                        else
                            listener.onError("Email is not registered")
                    }
            }
        }
    }
}