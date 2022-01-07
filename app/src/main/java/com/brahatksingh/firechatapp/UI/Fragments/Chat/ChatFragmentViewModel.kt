package com.brahatksingh.firechatapp.UI.Fragments.Chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brahatksingh.firechatapp.Data.FirebaseRepository
import com.brahatksingh.firechatapp.Data.Models.ChatMessage
import com.brahatksingh.firechatapp.Data.Repository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChatFragmentViewModel() : ViewModel() {

    private val _list = MutableLiveData<ArrayList<ChatMessage>>()
    val list : LiveData<ArrayList<ChatMessage>>
    get() = _list

    init {
        _list.value = ArrayList<ChatMessage>()
    }

    suspend fun getAllChatMessage(uid : String,sp_uid : String) {
        _list.value = Repository.getAllChatMessagesFromFirebase(uid,sp_uid)
    }

    suspend fun attachListener(uid :String,sp_uid: String) {
        Log.d("CHAT FIREBASE REPOSITORY","IN FUNCTION")
        val ref = FirebaseDatabase.getInstance().reference.child("messages").child(uid).child(sp_uid)
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d("CHAT FIREBASE REPOSITORY","$newMessage")
                if(newMessage != null) {
                    _list.value!!.add(newMessage)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        })
    }

    suspend fun sendMessage(message : String , uid : String, sp_uid: String) {
        Repository.sendMessageFromFirebase(message,uid,sp_uid)
    }
}