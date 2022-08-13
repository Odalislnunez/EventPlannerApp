package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        _binding.btnRegister.setOnClickListener {
            val name = _binding.etName.text.toString()
            val lastname = _binding.etLastname.text.toString()
            val phone = _binding.etPhone.text.toString()
            val email = _binding.etEmail.text.toString()
            val pass = _binding.etPassword.text.toString()
            val confirmPass = _binding.etConfirmPassword.text.toString()

            if(name.isNotEmpty() && lastname.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if(pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showLoginActiviy()
                        }
                        else {
                            showAlert(this.getString(R.string.error) + " " +  it.exception.toString())
                        }
                    }
                }
                else {
                    showError(this.getString(R.string.password_not_matching))
                }
            }
            else {
                showError(this.getString(R.string.complete_everything))
            }
        }

        _binding.tvAccount.setOnClickListener {
            showLoginActiviy()
        }
    }

    // TO GO LOG IN ACTIVITY.
    private fun showLoginActiviy() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun ViewLogin(view: View) {
        showLoginActiviy()
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
}