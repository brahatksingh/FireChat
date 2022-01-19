package com.brahatksingh.firechatapp.UI.Activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brahatksingh.firechatapp.Data.ChatDatabase
import com.brahatksingh.firechatapp.Data.FirebaseRepository
import com.brahatksingh.firechatapp.Data.Models.UserInfo
import com.brahatksingh.firechatapp.Data.Repository
import com.brahatksingh.firechatapp.NavGraphDirections
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.UI.Fragments.RecentChats.RecentChatsFragmentDirections
import com.brahatksingh.firechatapp.databinding.ActivityMainBinding
import com.brahatksingh.firechatapp.databinding.FragmentRecentChatsBinding
import com.brahatksingh.firechatapp.databinding.NavHeaderLayoutBinding
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding : ActivityMainBinding
    lateinit var firebaseAuth : FirebaseAuth
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    private val TAG = "MAIN ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        firebaseAuth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController =navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph,binding.drawerLayout)
        binding.mainactivityNavigationView.setupWithNavController(navController)
        binding.mainactivityNavigationView.setNavigationItemSelectedListener(this)
        setupActionBarWithNavController(navController,appBarConfiguration)
        setDrawerLayoutInfo()
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setDrawerLayoutInfo() {
        binding.mainactivityNavigationView.removeHeaderView(binding.mainactivityNavigationView.getHeaderView(0))
        val dl = binding.mainactivityNavigationView.inflateHeaderView(R.layout.nav_header_layout)
        val dl_name = dl.findViewById<TextView>(R.id.drawer_name)
        val dl_email = dl.findViewById<TextView>(R.id.drawer_email)
        val dl_uid = dl.findViewById<TextView>(R.id.drawer_uid)
        val dl_imv = dl.findViewById<ImageView>(R.id.drawer_imv)
        //bindingdl.drawerName.text = "OE40912"
        lifecycleScope.launch(Dispatchers.Main) {
            var userInfo = UserInfo()
            val temp = Repository.getUserInfoFromFirebase(firebaseAuth.uid)
            if(temp.equals("-1")) {
                    // Return
            }
            else {
                Log.d(TAG,"&&& $userInfo")
                userInfo = temp as UserInfo
                dl_name.setText(userInfo.name)
                dl_email.setText(userInfo.email)
                dl_uid.setText(userInfo.userId)
                Glide.with(applicationContext).load(userInfo.profilePicUrl).into(dl_imv)
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_navDrawer_signOut -> {
                firebaseAuth.signOut()
                Toast.makeText(this,"Signing Out...",Toast.LENGTH_LONG).show()
                lifecycleScope.launch(Dispatchers.Main) {
                    firebaseAuth.signOut()
                    async(Dispatchers.IO) {
                        Repository.deleteAllDataInDB()
                    }.await()
                    val intent = Intent(applicationContext,LogInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.newMessageFragment -> {
                navController.navigate(NavGraphDirections.actionGlobalNewMessageFragment(firebaseAuth!!.uid ?: "oubfoioqihfnh0"))
                binding.root.closeDrawer(GravityCompat.START)
            }
            else -> {
                // Nothing
            }
        }
        return true
    }

}