package com.dalhousie.rapid_crypto_app.viewmodels.loginViewModels

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModel
import com.dalhousie.rapid_crypto_app.models.User
import com.dalhousie.rapid_crypto_app.persistence.dao.AuthResultCallBacks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginViewModel(private val listener:AuthResultCallBacks):ViewModel(){

    private val user : User = User("","")

    private lateinit var firebaseAuth: FirebaseAuth

    //TextView listener bound to activity_login.xml
    val emailTextWatcher:TextWatcher
    get() = object:TextWatcher {
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

    //TextView listener bound to activity_login.xml
    val passwordTextWatcher:TextWatcher
    get() = object:TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Does nothing intentionally
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Does nothing intentionally
        }

        override fun afterTextChanged(p0: Editable?) {
            user.setPassword(p0.toString().trim())
        }
    }

    fun onForgotPassClicked(v: View){
        listener.onForgotPassword()
    }

    fun onRegistrationClicked(v: View) {
        listener.onRegistration()
    }

    fun onLoginClicked(v: View) {
        val loginCode:Int = user.isDataValid()

        firebaseAuth = FirebaseAuth.getInstance()

        when (loginCode) {
            //validates user email and password user inputs
            0 -> listener.onError("Please enter your email")
            1 -> listener.onError("Please enter a correct email")
            2 -> listener.onError("Please enter your password")
            else -> {
                firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (firebaseAuth.currentUser?.isEmailVerified == true) {
                                listener.onSuccess("Login Successful")
                            } else if (firebaseAuth.currentUser?.isEmailVerified != true) {
                                listener.onError("Please verify your email")
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        if (e is FirebaseAuthInvalidCredentialsException)
                            listener.onError("Invalid password")
                        else if (e is FirebaseAuthInvalidUserException) {

                            when (e.errorCode) {
                                //if user has been deleted in the Firebase console
                                "ERROR_USER_NOT_FOUND" -> listener.onError("Email is not registered")
                                //if user has been disabled in the Firebase console
                                "ERROR_USER_DISABLED" -> listener.onError("User account has been disabled")
                                else -> listener.onError(e.localizedMessage)
                            }
                        } else {
                            listener.onError(e.localizedMessage)
                        }
                    }
            }
        }
    }
}


