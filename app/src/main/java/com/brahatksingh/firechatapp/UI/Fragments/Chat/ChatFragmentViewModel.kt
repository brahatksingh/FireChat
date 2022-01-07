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
    private val _flag = MutableLiveData<Boolean>(true)
    val flag : LiveData<Boolean>
    get() = _flag

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
                if(newMessage != null) {
                    Log.d("CHAT VIEW MODEL","The new message $newMessage")
                    if(_list.value != null) {
                        Log.d("CHAT VIEW MODEL","ADDING TO LIST $newMessage")
                        _list.value!!.add(newMessage)
                        if(_flag.value != null) {
                            _flag.value = _flag.value?.not()
                        }
                    }
                    else {
                        Log.d("CHAT VIEW MODEL","ADDING LIST PROBLEM")
                    }
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