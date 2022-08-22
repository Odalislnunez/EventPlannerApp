package es.usj.mastertsa.onunez.eventplannerapp.presentation.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
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

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

//        headerName.text = userName
//        headerEmail.text = email

        val navMenu: Menu = navView.menu
        navMenu.findItem(R.id.nav_log_out)
            .setOnMenuItemClickListener {
                showAlert(getString(R.string.message_close))
                false
            }
    }

    // TO SHOW ALERT MESSAGE.
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        builder.setMessage(message)
        builder.setPositiveButton(R.string.button_OK) { _, _ ->
            logout()
        }
        builder.setNegativeButton(this.getString(R.string.button_CANCEL), null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // TO SIGN OUT APPLICATION.
    private fun logout() {
        LoginManager.getInstance().logOut()
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
//        onBackPressed()
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
