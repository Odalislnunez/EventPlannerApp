package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.ActivityLoginBinding
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.LoginViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.SHARED_EMAIL
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.SHARED_PASSWORD
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_NOT_EXISTS
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.WRONG_PASSWORD
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.isInputEmpty
import es.usj.mastertsa.onunez.eventplannerapp.utils.showAlert
import es.usj.mastertsa.onunez.eventplannerapp.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: LoginViewModel by viewModels()

//    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initObserves()
        initListeners()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null) {
            showMainActivity(firebaseAuth.currentUser?.uid?: "", firebaseAuth.currentUser?.email?: "", firebaseAuth.currentUser?.displayName?: "")
        }
    }

    private fun initObserves() {
        viewModel.loginState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    viewModel.getUserData()
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    manageLoginErrorMessages(dataState.exception)
                }
                is DataState.Loading ->{
                    showProgressBar()
                }
                else -> Unit
            }
        })

        viewModel.userDataState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    hideProgressDialog()
                    manageUserLogin()
                    startActivity(Intent(this, MainActivity::class.java))
                    this.finish()
                }
                is DataState.Error -> {
                    hideProgressDialog()
                    manageLoginErrorMessages(dataState.exception)
                }
                is DataState.Loading ->{
                    showProgressBar()
                }
                else -> Unit
            }
        })
    }

    private fun initListeners() {
        // TO LOG IN WITH EMAIL AND PASSWORD.
        _binding.btnLogin.setOnClickListener {
            loginUser(0)
        }

        // TO LOG IN WITH FACEBOOK ACCOUNT.
        _binding.ivFacebook.setOnClickListener {
            loginUser(1)
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
//
//            LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult> {
//                    override fun onSuccess(result: LoginResult) {
//                        result.let { it ->
//                            val token = it.accessToken
//
//                            val credential = FacebookAuthProvider.getCredential(token.token)
//
//                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
//                                if (it.isSuccessful) {
//                                    showMainActivity(it.result?.user?.uid?: "", it.result?.user?.email?: "", it.result?.user?.displayName?: "")
//                                } else {
//                                    showAlert(getString(R.string.error) + " " + it.exception.toString())
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onCancel() { TODO("Not yet implemented") }
//                    override fun onError(error: FacebookException) { TODO("Not yet implemented") }
//                })
        }

        // TO LOG IN WITH GOOGLE ACCOUNT.
        _binding.ivGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        // TO LOG IN WITH TWITTER ACCOUNT.


        // TO GO SIGN UP ACTIVITY.
        _binding.tvDontAccount.setOnClickListener {
            showSignUpActivity()
        }

        _binding.ivRegister.setOnClickListener {
            showSignUpActivity()
        }
    }

    private fun showSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun manageUserLogin(){
        sharedPreferences.edit().putString(SHARED_EMAIL, _binding.etUserlog.text.toString().trim()).apply()
        sharedPreferences.edit().putString(SHARED_PASSWORD, _binding.etPasswordlog.text.toString().trim()).apply()
    }

    private fun loginUser(type: Int){
        if (isUserDataOk()){
            showProgressBar()

            val email = _binding.etUserlog.text.toString().trim()
            val password = _binding.etPasswordlog.text.toString().trim()

            viewModel.login(email, password, type)
        }
    }

    private fun isUserDataOk(): Boolean{
        return when{
            isInputEmpty(_binding.etUserlog, true) -> {
                showAlert(this.getString(R.string.login_error_enter_email))
                false
            }

            isInputEmpty(_binding.etPasswordlog, true) -> {
                showAlert(this.getString(R.string.login_error_enter_password))
                false
            }

            else ->{
                true // Usuario ingresó todos los datos
            }
        }
    }

    private fun manageLoginErrorMessages(exception: Exception) {
        when(exception.message){
            USER_NOT_EXISTS -> { showAlert(this.getString(R.string.login_error_user_no_registered)) }
            WRONG_PASSWORD -> { showAlert(this.getString(R.string.login_error_wrong_password)) }
            else -> { showAlert(this.getString(R.string.error)) }
        }
    }

    // TO GO MAIN ACTIVITY.
    private fun showMainActivity(userId: String, email: String, userName: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("userId", userId)
            putExtra("email", email)
            putExtra("userName", userName)
        }
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if(account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showMainActivity(account.id?: "", account.email?: "", account.givenName?: "")
                        } else {
                            showAlert(this.getString(R.string.error) + " " +  it.exception.toString())
                        }
                    }
                }
            } catch(e: ApiException) {
                showAlert(this.getString(R.string.error) + " " +  e.message.toString())
            }
        }
    }

    private fun hideProgressDialog() {
        _binding.pbLogin.visibility = View.GONE
        _binding.btnLogin.text = this.getString(R.string.login_in)
        _binding.btnLogin.isEnabled = true
    }

    private fun showProgressBar() {
        _binding.btnLogin.text = ""
        _binding.btnLogin.isEnabled = false
        _binding.pbLogin.visibility = View.VISIBLE
    }
}