package com.brahatksingh.firechatapp.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.databinding.ActivityLogInBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FoldingCube
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLogInBinding
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val progressBar = binding.signinPb as ProgressBar
        val cube : Sprite = FoldingCube()
        progressBar.indeterminateDrawable = cube

        if(firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signinBtnToSignUp.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signinBtnSigninwithemail.setOnClickListener {
            var email :String = binding.signinEtvEmail.text.toString()
            if( email.isEmpty() || email == Patterns.EMAIL_ADDRESS.toString() ) {
                binding.signinEtvEmail.error = "Invalid Email"
                binding.signinEtvEmail.requestFocus()
                return@setOnClickListener
            }

            var password : String = binding.signinEtvPassword.text.toString()

            if(password.isEmpty() || password.length < 6 ) {
                binding.signinEtvPassword.error = "Invalid Password"
                binding.signinEtvPassword.requestFocus()
                return@setOnClickListener
            }
            // All is okay.
            binding.signinPb.visibility = View.VISIBLE
            signin(email,password)
        }

    }

    private fun signin(email : String,password : String ) : Unit {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task->
            if(task.isSuccessful) {
                Toast.makeText(this,"Sign In Successful",Toast.LENGTH_SHORT).show()
                binding.signinPb.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                binding.signinPb.visibility = View.GONE
                Toast.makeText(this,"An Error Occurred. Try Again.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}