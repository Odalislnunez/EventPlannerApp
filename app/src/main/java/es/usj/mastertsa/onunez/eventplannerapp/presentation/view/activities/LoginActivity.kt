package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        _binding.btnLogin.setOnClickListener {
            val email = _binding.etUserlog.text.toString()
            val pass = _binding.etPasswordlog.text.toString()

            if(!email.isNullOrEmpty() && !pass.isNullOrEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMainActivity()
                    }
                    else {
                        showError(it.exception.toString())
                    }
                }
            }
            else {
                showError(this.getString(R.string.complete_everything))
            }
        }

        _binding.tvDontAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        _binding
    }

    fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun showError(exception: String) {
        Toast.makeText(this, exception, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun ViewRegister(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun ViewFacebook(view: View) {

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

        LoginManager.getInstance().registerCallback(callbackManager,
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                result.let {
                    val token = it.accessToken

                    val credential = FacebookAuthProvider.getCredential(token.token)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showMainActivity()
//                            showHome(it.result?.user?.email ?: "", ProviderType.FACEBOOK)
                        } else {
                            showError(it.exception.toString())
//                            showAlert()
                        }
                    }
                }
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException) {
                TODO("Not yet implemented")
            }
        })
    }

    fun ViewGoogle(view: View) {
        // Configuration

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)

        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }
}