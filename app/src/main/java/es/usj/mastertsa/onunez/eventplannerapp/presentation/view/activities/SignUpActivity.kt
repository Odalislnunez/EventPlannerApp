package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.ActivitySignUpBinding
import es.usj.mastertsa.onunez.eventplannerapp.domain.models.User
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.SignUpViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.*
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.EMAIL_ALREADY_EXISTS

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.signUpState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<User> -> {
                    viewModel.saveUserToFirestore(user = dataState.data)
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    manageRegisterErrorMessages(dataState.exception)
                }
                is DataState.Loading ->{
                    showProgressBar()
                }
                else -> Unit
            }
        })
        viewModel.saveUserState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    showToast(this.getString(R.string.signup_successfully))
                    onBackPressed()
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    manageRegisterErrorMessages(dataState.exception)
                }
                is DataState.Loading ->{
                    showProgressBar()
                }
                else -> { }
            }
        })
    }

    private fun initListeners() {
        _binding.btnRegister.setOnClickListener {
            if (isUserDataOk()) {
                viewModel.signUp(createUser(), _binding.etPassword.text.toString())
            }
        }

        _binding.tvAccount.setOnClickListener {
            this.onBackPressed()
        }

        _binding.ivLogin.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun createUser(): User {
        val email = _binding.etEmail.text.toString()
        val name = _binding.etName.text.toString()
        val lastname = _binding.etLastname.text.toString()
        val phone = _binding.etPhone.text.toString()

        return User(
            email = email,
            name = name,
            lastname = lastname,
            phoneNumber = phone,
            userType = false
        )
    }

    private fun isUserDataOk() : Boolean{
        return when {
            isInputEmpty(_binding.etName, true) -> false
            isInputEmpty(_binding.etLastname, true) -> false
            isInputEmpty(_binding.etEmail, true) -> false

            isPasswordInsecure() -> {
                showAlert(this.getString(R.string.password_not_matching))
                false
            }
            else -> true

        }
    }

    private fun isPasswordInsecure(): Boolean{

        return if (_binding.etPassword.text.toString().length <= 5){
            showAlert(this.getString(R.string.signup_password_insecure))
            true
        } else {
            _binding.etPassword.text.toString() != _binding.etConfirmPassword.text.toString()
        }
    }

    private fun manageRegisterErrorMessages(exception: Exception) {
        if (exception.toString() == EMAIL_ALREADY_EXISTS) {
            showAlert(this.getString(R.string.signup_email_already_registered))
        } else {
            showAlert(this.getString(R.string.error))
        }
    }

    private fun hideProgressDialog() {
        _binding.pbRegister.visibility = View.GONE
        _binding.btnRegister.visibility = View.VISIBLE
        _binding.btnRegister.isEnabled = true
    }

    private fun showProgressBar() {
        _binding.btnRegister.isEnabled = false
        _binding.pbRegister.visibility = View.VISIBLE
        _binding.btnRegister.visibility = View.GONE
    }
}