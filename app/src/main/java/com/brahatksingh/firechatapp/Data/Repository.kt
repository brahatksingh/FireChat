package com.brahatksingh.firechatapp.Data

import androidx.lifecycle.MutableLiveData
import com.brahatksingh.firechatapp.Data.Models.ChatMessage
import com.brahatksingh.firechatapp.Data.Models.UserInfo

object Repository {

    suspend fun getAllUsersFromFirebase() : ArrayList<UserInfo> {
        return FirebaseRepository.getAllUsers()
    }

    suspend fun getAllChatMessagesFromFirebase(uid: String ,sp_uid : String) : ArrayList<ChatMessage> {
        return FirebaseRepository.getALLMessagesOfChat(uid,sp_uid)
    }

    suspend fun sendMessageFromFirebase(message: String,uid : String,sp_uid : String) {
        FirebaseRepository.sendMessage(message,uid,sp_uid)
    }

}