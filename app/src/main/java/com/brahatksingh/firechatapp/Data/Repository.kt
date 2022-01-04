package com.brahatksingh.firechatapp.Data

import com.brahatksingh.firechatapp.Data.Models.UserInfo

object Repository {

    suspend fun getAllUsersFromFirebase() : ArrayList<UserInfo> {
        return FirebaseRepository.getAllUsers()
    }

}