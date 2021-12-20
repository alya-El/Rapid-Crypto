package com.dalhousie.rapid_crypto_app.fragments.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dalhousie.rapid_crypto_app.LoginActivity
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.databinding.FragmentRegisterBinding
import com.dalhousie.rapid_crypto_app.persistence.dao.AuthResultCallBacks
import com.dalhousie.rapid_crypto_app.viewmodels.registrationViewModels.RegisterUserViewModel
import com.dalhousie.rapid_crypto_app.viewmodels.registrationViewModels.RegisterUserViewModelFactory

class RegisterUserFragment : Fragment(), AuthResultCallBacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentRegisterUserBinding = DataBindingUtil.setContentView<FragmentRegisterBinding>(requireActivity(),R.layout.fragment_register)
        //binding our RegisterUserViewModel to RegisterUserFragment
        fragmentRegisterUserBinding.viewModel = ViewModelProviders.of(this, RegisterUserViewModelFactory(this)).get(
            RegisterUserViewModel::class.java)

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

    /*private lateinit var registerUserViewModel: RegisterUserViewModel
    private var _binding: FragmentRegisterBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerUserViewModel =
            ViewModelProvider(this).get(RegisterUserViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        // Onclick listener for the register button and gathers user input
        view.findViewById<Button>(R.id.button_register)?.setOnClickListener{
            val userEmail = view.findViewById<EditText>(R.id.input_email).text.toString()
            val userPassword = view.findViewById<EditText>(R.id.input_password).text.toString()
            val confirmedPassword = view.findViewById<EditText>(R.id.input_password_confirm).text.toString()
            val user = User(userEmail.replace("\\s".toRegex(), ""), userPassword)
            // Check user input before registering in Firebase
            if(checkInputs(user, confirmedPassword)== "Registration Successful")
                registerUser(user)
            else
                Toast.makeText(this.context, checkInputs(user, confirmedPassword),
                    Toast.LENGTH_SHORT).show()
        }

        view.findViewById<TextView>(R.id.text_LoginNow).setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    // Uses User class method to check data accuracy
    private fun checkInputs(user: User, confirmPassword : String): String {
        var snackbarText :String = when (user.isDataValid()) {
            0 -> "Please enter your email"
            1 -> "Please enter a correct email"
            2 -> "Please enter your password"
            else -> {
                "Registration Successful"
            }
        }
        // Ensure both entered passwords match
        if (user.getPassword() != confirmPassword)
            snackbarText = "Passwords do not match"

        return snackbarText
    }

    private fun registerUser(user: User) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Signup and sign in was successful -> go to main screen
                    registerUserViewModel.createProfile(user.getEmail())
                    activity?.let {
                        val intent = Intent(it, MainActivity::class.java)
                        it.startActivity(intent)
                    }
                } else {
                    // Sign in failed -> display toast message to user
                    Toast.makeText(this.context, task.exception?.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}
