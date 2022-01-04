package com.brahatksingh.firechatapp.Data

import android.util.Log
import com.brahatksingh.firechatapp.Data.Models.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object FirebaseRepository {

    private val TAG = "FIREBASE REPOSITORY : "
    private val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase :FirebaseDatabase = FirebaseDatabase.getInstance()

    suspend fun getAllUsers() : ArrayList<UserInfo> {
        var userList = ArrayList<UserInfo>()
        Log.d(TAG,"BEFORE")

        val userRef = firebaseDatabase.reference.child("users")
        userRef.get().addOnSuccessListener { snapshot->
            if(snapshot.exists()) {
                Log.d(TAG,"$snapshot")
                val tempList = ArrayList<UserInfo>()
                for(child in snapshot.children){
                    val temp = child.getValue(UserInfo::class.java)
                    if(!temp?.userId!!.equals(firebaseAuth.currentUser!!.uid)) {
                        tempList.add(temp)
                    }
                }
                userList = tempList
            }
        }.await()

        Log.d(TAG,"AFTER")
        return userList
    }
}