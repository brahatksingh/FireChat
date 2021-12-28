package com.brahatksingh.firechatapp.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.brahatksingh.firechatapp.R
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.async {
            async {
                delay(3000)
                val intent = Intent(applicationContext,LogInActivity::class.java)
                startActivity(intent)
                finish()
            }.await()
        }

    }
}