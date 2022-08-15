package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

        // TO LOG IN WITH EMAIL AND PASSWORD.
        _binding.btnLogin.setOnClickListener {
            val email = _binding.etUserlog.text.toString()
            val pass = _binding.etPasswordlog.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        showMainActivity(it.result?.user?.uid?: "", it.result?.user?.email?: "", it.result?.user?.displayName?: "")
                    }
                    else {
                        showAlert(this.getString(R.string.error) + " " +  it.exception.toString())
                    }
                }
            }
            else {
                showError(this.getString(R.string.complete_everything))
            }
        }

        // TO GO SIGN UP ACTIVITY.
        _binding.tvDontAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null) {
            showMainActivity(firebaseAuth.currentUser?.uid?: "", firebaseAuth.currentUser?.email?: "", firebaseAuth.currentUser?.displayName?: "")
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

    // TO SHOW TOAST ERROR MESSAGE.
    private fun showError(exception: String) {
        Toast.makeText(this, exception, Toast.LENGTH_SHORT).show()
    }

    // TO SHOW ALERT ERROR MESSAGE.
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton(this.getString(R.string.button_OK), null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // TO GO SIGN UP ACTIVITY.
    fun ViewRegister(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    // TO LOG IN WITH FACEBOOK ACCOUNT.
    fun ViewFacebook(view: View) {

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

        LoginManager.getInstance().registerCallback(callbackManager,
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                result.let { it ->
                    val token = it.accessToken

                    val credential = FacebookAuthProvider.getCredential(token.token)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showMainActivity(it.result?.user?.uid?: "", it.result?.user?.email?: "", it.result?.user?.displayName?: "")
                        } else {
                            showAlert(getString(R.string.error) + " " + it.exception.toString())
                        }
                    }
                }
            }

            override fun onCancel() { TODO("Not yet implemented") }
            override fun onError(error: FacebookException) { TODO("Not yet implemented") }
        })
    }

    // TO LOG IN WITH GOOGLE ACCOUNT.
    fun ViewGoogle(view: View) {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()

        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
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

    // TO LOG IN WITH TWITTER ACCOUNT.
    fun ViewTwitter(view: View) {

    }
}