package com.brahatksingh.firechatapp.UI.Activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brahatksingh.firechatapp.Data.FirebaseRepository
import com.brahatksingh.firechatapp.Data.Repository
import com.brahatksingh.firechatapp.NavGraphDirections
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var firebaseAuth : FirebaseAuth
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        firebaseAuth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController =navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.newMessageFragment),binding.drawerLayout)
        setupActionBarWithNavController(navController)

        binding.mainactivityNavigationView.setupWithNavController(navController)

        val firebaseSRC = FirebaseRepository(lifecycleScope)
        val repository = Repository(firebaseSRC)
        lifecycleScope.launch {
            val userList = repository.getAllUsersFromFirebase()
            Log.d("SIUFFVFFFF","$userList")
            Toast.makeText(applicationContext,"$userList",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}