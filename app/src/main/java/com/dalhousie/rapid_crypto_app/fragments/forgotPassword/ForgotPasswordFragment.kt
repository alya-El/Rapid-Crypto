package com.dalhousie.rapid_crypto_app.fragments.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dalhousie.rapid_crypto_app.LoginActivity
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.databinding.FragmentForgotPasswordBinding
import com.dalhousie.rapid_crypto_app.persistence.dao.AuthResultCallBacks
import com.dalhousie.rapid_crypto_app.viewmodels.forgotPasswordViewModels.ForgotPasswordViewModel
import com.dalhousie.rapid_crypto_app.viewmodels.forgotPasswordViewModels.ForgotPasswordViewModelFactory


class ForgotPasswordFragment() : Fragment(), AuthResultCallBacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentForgotPasswordBinding = DataBindingUtil.setContentView<FragmentForgotPasswordBinding>(requireActivity(),R.layout.fragment_forgot_password)
        //binding our ForgotPasswordViewModel to ForgotPasswordFragment
        fragmentForgotPasswordBinding.viewModel = ViewModelProviders.of(this, ForgotPasswordViewModelFactory(this)).get(
            ForgotPasswordViewModel::class.java)

        //if user presses back button, they are directed to the login page
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.let {
                    val intent = Intent(it, LoginActivity::class.java)
                    it.startActivity(intent)
                }
            }
        })
    }

    override fun onSuccess(message: String) {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
        activity?.let {
            val intent = Intent(it, LoginActivity::class.java)
            it.startActivity(intent)
        }
    }

    override fun onError(message: String) {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }
}