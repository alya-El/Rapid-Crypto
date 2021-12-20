package com.dalhousie.rapid_crypto_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.dalhousie.rapid_crypto_app.databinding.ActivityLoginBinding
import com.dalhousie.rapid_crypto_app.fragments.forgotPassword.ForgotPasswordFragment
import com.dalhousie.rapid_crypto_app.fragments.registration.RegisterUserFragment
import com.dalhousie.rapid_crypto_app.persistence.dao.AuthResultCallBacks
import com.dalhousie.rapid_crypto_app.viewmodels.loginViewModels.LoginViewModel
import com.dalhousie.rapid_crypto_app.viewmodels.loginViewModels.LoginViewModelFactory
import com.google.android.material.snackbar.Snackbar


class LoginActivity : AppCompatActivity(), AuthResultCallBacks{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityLoginBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,R.layout.activity_login)
        //binding our LoginViwModel to LoginActivity
        activityLoginBinding.viewModel = ViewModelProviders.of(this, LoginViewModelFactory(this)).get(
            LoginViewModel::class.java)
    }

    override fun onSuccess(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onError(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    override fun onForgotPassword() {
        supportFragmentManager.beginTransaction().replace(R.id.container, ForgotPasswordFragment()::class.java, null).addToBackStack(null).commit()
    }

    override fun onRegistration() {
        supportFragmentManager.beginTransaction().replace(R.id.container, RegisterUserFragment()::class.java, null).addToBackStack(null).commit()
    }
}

