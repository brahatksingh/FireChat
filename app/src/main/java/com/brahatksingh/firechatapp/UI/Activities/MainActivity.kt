package com.brahatksingh.firechatapp.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding : ActivityMainBinding
    lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this,binding.root,R.string.open_maina,R.string.close_maina)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()

        firebaseAuth = FirebaseAuth.getInstance()
        binding.userEmailInMainActivity.text = firebaseAuth.currentUser?.uid

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mainactivityNavigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_item_navDrawer_signOut -> {
                    Toast.makeText(this,"SIGNING OUT",Toast.LENGTH_SHORT).show()
                    firebaseAuth.signOut()
                    val intent = Intent(this,LogInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.menu_item_navDrawer_newMessage -> {
                    Toast.makeText(this,"NEW MESSAGE",Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}