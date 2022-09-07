package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.LoginViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.SHARED_EMAIL
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.SHARED_LOGIN_TYPE
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.SHARED_PASSWORD
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showAlert
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObserves()

        if (isUserSaved()){
            viewModel.login(getSavedEmail()!!, getSavedPassword()!!, getLoginType()!!.toInt(), this)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun initObserves(){
        viewModel.loginState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    viewModel.getUserData()
                }
                is DataState.Error -> {
                    showAlert(getString(R.string.error))
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                else -> Unit
            }
        })

        viewModel.userDataState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is DataState.Error -> {
                    showAlert(getString(R.string.error))
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                else -> Unit
            }
        })
    }

    private fun isUserSaved(): Boolean{
        return getSavedEmail()?.isNotEmpty() == true && getSavedPassword()?.isNotEmpty() == true && getLoginType()?.isNotEmpty() == true
    }

    private fun getSavedEmail() = sharedPreferences.getString(SHARED_EMAIL, "")
    private fun getSavedPassword() = sharedPreferences.getString(SHARED_PASSWORD, "")
    private fun getLoginType() = sharedPreferences.getString(SHARED_LOGIN_TYPE, "")
}