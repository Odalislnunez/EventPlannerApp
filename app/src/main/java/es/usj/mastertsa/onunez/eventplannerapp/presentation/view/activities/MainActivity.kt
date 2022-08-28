package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import es.usj.mastertsa.onunez.eventplannerapp.R
import es.usj.mastertsa.onunez.eventplannerapp.databinding.ActivityMainBinding
import es.usj.mastertsa.onunez.eventplannerapp.presentation.viewmodel.LoginViewModel
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_EMAIL
import es.usj.mastertsa.onunez.eventplannerapp.utils.Constants.USER_LOGGED_IN_NAME
import es.usj.mastertsa.onunez.eventplannerapp.utils.DataState
import es.usj.mastertsa.onunez.eventplannerapp.utils.showAlert
import es.usj.mastertsa.onunez.eventplannerapp.utils.showAlertWithNegative

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserves()
        initListeners()
    }

    private fun initObserves() {
        viewModel.logOutState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    this.finish()
                }
                is DataState.Error -> {
                    showAlert(this.getString(R.string.error) + " " + dataState.exception)
                }
                else -> Unit
            }
        })
    }

    private fun initListeners() {
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_profile, R.id.nav_events, R.id.nav_contacts, R.id.nav_invitations, R.id.nav_public_events, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val header = navView.getHeaderView(0)

        val headerName: TextView = header.findViewById(R.id.tv_userName)
        val headerEmail: TextView = header.findViewById(R.id.tv_email)

        headerName.text = USER_LOGGED_IN_NAME
        headerEmail.text = USER_LOGGED_IN_EMAIL

        val navMenu: Menu = navView.menu
        navMenu.findItem(R.id.nav_log_out)
            .setOnMenuItemClickListener {
                if(showAlertWithNegative(getString(R.string.message_close))) {
                    viewModel.logOut()
                }
                false
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
