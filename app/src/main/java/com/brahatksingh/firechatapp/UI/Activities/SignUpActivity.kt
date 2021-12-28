package com.brahatksingh.firechatapp.UI.Activities

import android.content.ContentResolver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import android.net.Uri
import android.util.Log
import com.brahatksingh.firechatapp.Data.Models.UserInfo


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase
    val TAG ="SIGNUPATAG"
    var selectedPhoto : Uri? = null
    var IMAGE_RESPONE_CODE = 1;
    var isOk = false;
    val imageUrl : String = "."
    var userUID = "."
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signupTvSelectPhoto.setOnClickListener {
            val intent = Intent();
            intent.type = "image/*"
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Pic"),IMAGE_RESPONE_CODE)
        }
        binding.signupBtnSignUp.setOnClickListener {
            val email = binding.signupEtvEmail.text.toString()
            if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signupEtvEmail.error = "Invalid Email Address"
                binding.signupEtvEmail.requestFocus()
                return@setOnClickListener
            }
            if(binding.signupEtvName.text.length < 3) {
                binding.signupEtvName.error= "Name should at least have 3 characters"
                binding.signupEtvName.requestFocus()
                return@setOnClickListener
            }
            val password = binding.signupEtvPassword.text.toString()
            if(password.length < 4) {
                binding.signupEtvPassword.error = "Password should at least have 4 characters."
                binding.signupEtvPassword.requestFocus()
                return@setOnClickListener
            }

            // All Okay
            Log.d(TAG,"STARTING PROCESS")
            binding.pbSignup.visibility = View.VISIBLE
            createAccount(email,password,binding.signupEtvName.text.toString())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) {
            if(data != null) {
                selectedPhoto = data?.data
                binding.signupImgvPhoto.setImageURI(selectedPhoto)
            }
            else {
                val context = this
                selectedPhoto = Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                        + context.getResources().getResourcePackageName(R.drawable.profilepicnormall) + '/'
                        + context.getResources().getResourceTypeName(R.drawable.profilepicnormall) + '/'
                        + context.getResources().getResourceEntryName(R.drawable.profilepicnormall) )
            }

        }
    }

    private fun createAccount(email : String, password : String,name:String) {
        val context = this
        selectedPhoto = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + context.getResources().getResourcePackageName(R.drawable.profilepicnormall) + '/'
                    + context.getResources().getResourceTypeName(R.drawable.profilepicnormall) + '/'
                    + context.getResources().getResourceEntryName(R.drawable.profilepicnormall) )
        lifecycleScope.async(Dispatchers.Main) {

            async {
                create(email,password)
            }.await()
            Log.d(TAG,"The isOk is $isOk")
            if(isOk){
                async {
                    Log.d(TAG,"in 1 async")
                    uploadImage()
                }.await()
                async {
                    Log.d(TAG,"in 2 async")
                    uploadDataToRealtimeDatabase(userUID,email,name,imageUrl)
                }.await()
                binding.pbSignup.visibility = View.GONE
                val intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding.pbSignup.visibility = View.GONE

        }
    }

    suspend fun create(email: String,password: String) {
        Log.d(TAG,"BEFORE")
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(parent) {task ->
            if(task.isSuccessful) {
                Toast.makeText(this@SignUpActivity,"SignUp Successful.",Toast.LENGTH_SHORT).show()
                isOk = true;
                userUID = firebaseAuth.currentUser!!.uid
                Log.d(TAG,"INSIDE")
                return@addOnCompleteListener
            }
            else {
                Log.d(TAG,"${task.exception} . ")
                Toast.makeText(this@SignUpActivity,"SignUp Not Successful.",Toast.LENGTH_SHORT).show()
            }
        }
        Log.d(TAG,"AFTER")
    }

    suspend fun uploadDataToRealtimeDatabase(UID:String,userEmail: String,userName : String,url:String) {
        Log.d(TAG,"in upload data")
        val ref = FirebaseDatabase.getInstance("https://firechat-931d2-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("/users/$UID")
        val userinfo = UserInfo(userEmail,UID,userName,url)
        ref.setValue(userinfo).addOnSuccessListener {
            Log.d(TAG,"UPLOADED USER INFORMATION")
        }.addOnFailureListener{
            Log.d(TAG,"${it.message} $it")
        }
    }

    suspend fun uploadImage() : String  {
        Log.d(TAG,"in upload Image")
        val profilePicName = "${firebaseAuth.uid}.profileImage"
        var url = "."
        val storage_reference = FirebaseStorage.getInstance("gs://firechat-931d2.appspot.com").getReference("/ProfileImages/$profilePicName")
        storage_reference.putFile(selectedPhoto!!).continueWithTask { task ->
            if (!task.isSuccessful) {
                Log.d(TAG,"${task.exception}")
            }
            storage_reference.downloadUrl.addOnSuccessListener {
                url = it.toString()
            }.addOnFailureListener{
                Log.d(TAG,"$it ${it.message}")
            }
        }

        if(url.length < 2) {
            Log.d(TAG,"Going with default url.")
            url = "https://firebasestorage.googleapis.com/v0/b/firechat-931d2.appspot.com/o/ProfileImages%2FsqE6s03wgXQm7gl03xxQIM3JVQc2.profileImage?alt=media&token=640266a5-6611-4e09-b8ed-72ba8bdfdc1f"

        }
        Log.d(TAG,"returning the img url $url")
        return url
    }

}