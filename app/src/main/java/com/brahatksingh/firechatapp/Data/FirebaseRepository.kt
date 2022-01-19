package com.brahatksingh.firechatapp.Data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.brahatksingh.firechatapp.Data.Models.ChatMessage
import com.brahatksingh.firechatapp.Data.Models.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

object FirebaseRepository {

    private val TAG = "FIREBASE REPOSITORY FR"
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase : FirebaseDatabase = FirebaseDatabase.getInstance()

    suspend fun getAllUsers() : ArrayList<UserInfo> {
        var userList = ArrayList<UserInfo>()
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

        return userList
    }

    suspend fun getALLMessagesOfChat(uid : String, sp_uid : String ): ArrayList<ChatMessage>{
        var chatMessages = ArrayList<ChatMessage>()
        var ref = firebaseDatabase.reference.child("messages").child(uid).child(sp_uid)
        ref.get().addOnSuccessListener { snapshot ->
            val tempList = ArrayList<ChatMessage>()
            for(child in snapshot.children) {
                if(child != null) {
                    tempList.add(child.getValue(ChatMessage::class.java) ?: ChatMessage(uid,sp_uid,"DEF_MESSAGE","0","1637428306",".") )
                }
            }
            chatMessages = tempList
        }.await()
        return chatMessages
    }

    suspend fun sendMessage(message: String,uid : String,sp_uid : String) {
        val messageObj = ChatMessage(uid,sp_uid,message,"0","${System.currentTimeMillis() /1000}",".")

        var ref = firebaseDatabase.reference.child("messages").child(uid).child(sp_uid).push()

        ref.setValue(messageObj).addOnSuccessListener {
            Log.d(TAG,"MESSAGE UPLOADED")
        }.addOnFailureListener{
            Log.d(TAG,"MESSAGE UPLOAD FAILED $it")
        }.await()

        ref = firebaseDatabase.reference.child("messages").child(sp_uid).child(uid).push()

        ref.setValue(messageObj).addOnSuccessListener {
            Log.d(TAG,"MESSAGE UPLOADED 2")
        }.addOnFailureListener{
            Log.d(TAG,"MESSAGE UPLOAD 2 FAILED $it")
        }.await()

    }

    suspend fun getUserInfo(uid: String) : UserInfo {
        var userInfo = UserInfo()
        val ref = firebaseDatabase.reference.child("users").child(uid)
        ref.get().addOnCompleteListener{ snapshot ->
            val temp = snapshot.to(UserInfo::class.java)
            userInfo = temp.second.newInstance()

        }.await()
        return userInfo
    }
}