package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            val email = _binding.etEmail.text.toString()
            val phone = _binding.etPhone.text.toString()
            val pass = _binding.etPassword.text.toString()
            val confirm_pass = _binding.etConfirmPassword.text.toString()

            if(!name.isNullOrEmpty() && !lastname.isNullOrEmpty() && !email.isNullOrEmpty() && !phone.isNullOrEmpty() && !pass.isNullOrEmpty() && !confirm_pass.isNullOrEmpty()) {
                if(pass == confirm_pass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this, this.getString(R.string.password_not_matching), Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, this.getString(R.string.complete_everything), Toast.LENGTH_SHORT).show()
            }
        }

        _binding.tvAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun ViewLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}