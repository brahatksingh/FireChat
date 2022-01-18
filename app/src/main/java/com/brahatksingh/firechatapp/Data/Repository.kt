package com.brahatksingh.firechatapp.Data

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brahatksingh.firechatapp.Data.Models.ChatMessage
import com.brahatksingh.firechatapp.Data.Models.RecentChatData
import com.brahatksingh.firechatapp.Data.Models.UserInfo
import kotlinx.coroutines.async

object Repository {
    lateinit var chatDao : ChatDao
    private val TAG = "REPOSITORY"

    suspend fun instantiateDAOofRepository(gotDAO : ChatDao) {
        chatDao = gotDAO
    }

    suspend fun getAllUsersFromFirebase() : ArrayList<UserInfo> {
        return FirebaseRepository.getAllUsers()
    }

    suspend fun getAllChatMessagesFromFirebase(uid: String ,sp_uid : String) : ArrayList<ChatMessage> {
        return FirebaseRepository.getALLMessagesOfChat(uid,sp_uid)
    }

    suspend fun sendMessageFromFirebase(message: String,uid : String,sp_uid : String) {
        FirebaseRepository.sendMessage(message,uid,sp_uid)
    }

    suspend fun getAllChatsFromDB() : LiveData<ArrayList<RecentChatData>> {

         val list = chatDao.getAllChats()
         val r = MutableLiveData<ArrayList<RecentChatData>>(list as ArrayList<RecentChatData>)
         return r as LiveData<ArrayList<RecentChatData>>
    }

    suspend fun insertDatainDB(newchat : RecentChatData) : Long {
        Log.d(TAG,"INSERTING NEW DATA $newchat")
        return chatDao.addChat(newchat)
    }

    suspend fun updateDatainDB(newchat : RecentChatData) {
        Log.d(TAG,"CREATING NEW DATA $newchat")
        chatDao.updateChat(newchat)
    }

    suspend fun updateDBwithID(id:Long,newtext : String) {
        Log.d(TAG,"UPDATING $id WITH $newtext")
        chatDao.updateWithChatID(id,newtext)
    }

    suspend fun searchUserInDB(gotUID : String) : Long {
        Log.d(TAG,"SEARCHING $gotUID")
        return chatDao.searchWithUID(gotUID)
    }

}