package com.brahatksingh.firechatapp.UI.Fragments.Chat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brahatksingh.firechatapp.Data.FirebaseRepository
import com.brahatksingh.firechatapp.Data.Models.ChatMessage
import com.brahatksingh.firechatapp.Data.Models.RecentChatData
import com.brahatksingh.firechatapp.Data.Repository
import com.google.firebase.database.*
import kotlinx.coroutines.*

class ChatFragmentViewModel() : ViewModel() {

    private val _list = MutableLiveData<ArrayList<ChatMessage>>()
    val list : LiveData<ArrayList<ChatMessage>>
    get() = _list
    private val _flag = MutableLiveData<Boolean>(true)
    val flag : LiveData<Boolean>
    get() = _flag
    private val TAG = "CHAT VIEW MODEL"
    private var firebaseRef : DatabaseReference? = null


    init {
        _list.value = ArrayList<ChatMessage>()
    }

    suspend fun getAllChatMessage(uid : String,sp_uid : String) {
        _list.value = Repository.getAllChatMessagesFromFirebase(uid,sp_uid)
    }

    suspend fun attachListener(uid :String,sp_uid: String) {
        Log.d(TAG,"ATTACH LISTENER FUNCTION")
        if(firebaseRef != null) {
            return
        }
        val ref = FirebaseDatabase.getInstance().reference.child("messages").child(uid).child(sp_uid)
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newMessage = snapshot.getValue(ChatMessage::class.java)
                if(newMessage != null) {
                    Log.d(TAG,"The new message $newMessage")
                    if(_list.value != null) {
                        Log.d(TAG,"ADDING TO LIST $newMessage")
                        _list.value!!.add(newMessage)
                        if(_flag.value != null) {
                            _flag.value = _flag.value?.not()
                        }
                    }
                    else {
                        Log.d(TAG,"ADDING IN LIST PROBLEM")
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

        firebaseRef = ref
    }

    suspend fun sendMessage(message : String , uid : String, sp_uid: String) {
        Repository.sendMessageFromFirebase(message,uid,sp_uid)
    }

    suspend fun updateLastMessageInDB(id : Long,message : String = "DEFVAL") {
        // All details are of the second user.
        Log.d(TAG,"UPDATING $id USER WITH $message")
        Repository.updateDBwithID(id,message)
    }

    suspend fun insertNewMessageInDB(name : String,picURL : String,uid : String,message : String = "DEF String") : Long {
        Log.d(TAG,"CREATING NEW USER")
        return Repository.insertDatainDB(RecentChatData(0,name,message,uid,picURL))
    }

    suspend fun findUserInDB(name : String,picURL : String,uid : String, message : String = "DEF String") {
        GlobalScope.launch {
            var DB_ID : Long = -1
            async {
                DB_ID = if(Repository.searchUserInDB(uid) == null) {
                    -1
                }
                else {
                    Repository.searchUserInDB(uid)
                }
            }.await()
            Log.d(TAG,"THE GOT DB ID IS $DB_ID")
            if(DB_ID < 0) {
                insertNewMessageInDB(name,picURL,uid,message)
            }
            else {
                updateLastMessageInDB(DB_ID,message)
            }
        }
    }

    suspend fun uploadImage(uri : Uri,uid: String,sp_uid: String) {
        Repository.uploadImageInFirebaseStrorage(uri,uid,sp_uid)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"VIEW MODEL DESTROYED")
    }

    suspend fun siddb(uid:String) {
        Log.d(TAG+"!!!!!!!","!!!!!!!!! ${Repository.searchUserInDB(uid)}")
    }
}