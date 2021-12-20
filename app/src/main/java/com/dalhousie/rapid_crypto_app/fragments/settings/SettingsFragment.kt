package com.dalhousie.rapid_crypto_app.fragments.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dalhousie.rapid_crypto_app.LoginActivity
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.databinding.FragmentSettingsBinding
import com.dalhousie.rapid_crypto_app.viewmodels.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<CardView>(R.id.profile_card).setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_navigation_profile)
        }

        view.findViewById<CardView>(R.id.delete_account_card).setOnClickListener {
            val alert: AlertDialog.Builder = AlertDialog.Builder(activity)

            alert.setTitle("Delete Account?")
            alert.setMessage("Are you sure you want to continue? This action is irreversible.")

            alert.setPositiveButton(
                "Continue"
            ) { _, _ ->
                // draws second alert and double checks user wants to delete account
                confirmDeletion()
            }

            alert.setNegativeButton(
                "Cancel"
            ) { _, _ -> }

            alert.show()
        }

        view.findViewById<Button>(R.id.sign_out_button).setOnClickListener {
            val alert: AlertDialog.Builder = AlertDialog.Builder(activity)

            alert.setTitle("Sign Out?")
            alert.setMessage("You will need to login again to access application features. Continue?")

            alert.setPositiveButton(
                "Sign Out"
            ) { _, _ ->
                FirebaseAuth.getInstance().signOut()
                // checks to make sure user has been signed out before redirecting
                if (FirebaseAuth.getInstance().currentUser == null) {
                    Toast.makeText(activity, "Sign Out Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(activity,LoginActivity::class.java))
                } else {
                    view.let {
                        Snackbar.make(
                            it,
                            "Something went wrong",
                            Snackbar.LENGTH_SHORT
                        )
                    }
                }
            }

            alert.setNegativeButton(
                "Cancel"
            ) { _, _ -> }

            alert.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun confirmDeletion() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(activity)
        alert.setTitle("Confirm Deletion")
        alert.setMessage("This action will permanently delete your account. Delete anyway?")

        alert.setPositiveButton(
            "Delete"
        ) {_, _ ->
            FirebaseAuth.getInstance().currentUser?.delete()
            startActivity(Intent(activity,LoginActivity::class.java))
        }

        alert.setNegativeButton(
            "Cancel"
        ) { _, _ -> }

        alert.show()
    }
}