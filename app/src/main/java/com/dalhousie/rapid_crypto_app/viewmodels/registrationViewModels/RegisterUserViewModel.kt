package com.dalhousie.rapid_crypto_app.viewmodels.registrationViewModels

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalhousie.rapid_crypto_app.models.User
import com.dalhousie.rapid_crypto_app.persistence.dao.AuthResultCallBacks
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.ProfileDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.repositories.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterUserViewModel(private val listener: AuthResultCallBacks) : ViewModel() {

    private val profileRepository: ProfileRepository

    init {
        val profileDAO = ProfileDAOImpl()
        profileRepository = ProfileRepository(profileDAO)
    }

    private lateinit var firebaseAuth: FirebaseAuth

    private var confirmPassword : String = ""

    private val user : User = User("","")

    val emailTextWatcher: TextWatcher
        get() = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                user.setEmail(p0.toString().replace("\\s".toRegex(), ""))
            }
        }

    val passwordTextWatcher: TextWatcher
        get() = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                user.setPassword(p0.toString().trim())
            }
        }

    val confirmPasswordTextWatcher: TextWatcher
        get() = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                confirmPassword = p0.toString().trim()
            }
        }

    fun onExistingUserClicked(v: View){
        listener.onLogin()
    }

    fun onRegisterClicked(v: View){
        var loginCode:Int = user.isDataValid()

        firebaseAuth = FirebaseAuth.getInstance()

        if (loginCode == 1) {
            listener.onError("Please enter a correct email")
        }
        else if(loginCode == 0) {
            listener.onError("Please enter your email")
        }
        else if (loginCode == 2) {
            listener.onError("Please enter your password")
        }
        else if (user.getPassword().length < 6) {
            listener.onError("Password must be at least 6 characters")
        }
        else if(confirmPassword != user.getPassword() && (user.getPassword() != null)){
            listener.onError("Both password fields must be identical")
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                            if (task.isSuccessful) {
                                listener.onSuccess("Verification email sent")
                                createProfile(user.getEmail())
                            }
                            else{
                                listener.onError("Email unable to sent. Please try again")
                            }
                        }
                    }
                    else
                        listener.onError("Email address is already registered")
                }
        }
    }

    /* Registers the user in the profile collection of the db after
     * being added as an Authenticated user
     */
    private fun createProfile(email: String) {
        viewModelScope.launch {
            profileRepository.createProfile(email)
        }
    }
    /*init {
        val profileDAO = ProfileDAOImpl()
        profileRepository = ProfileRepository(profileDAO)
    }

    /* Registers the user in the profile collection of the db after
     * being added as an Authenticated user
     */
    fun createProfile(email: String) {
        viewModelScope.launch {
            profileRepository.createProfile(email)
        }
    }*/
}