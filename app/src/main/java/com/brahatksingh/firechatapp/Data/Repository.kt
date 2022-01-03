package com.brahatksingh.firechatapp.Data

import com.brahatksingh.firechatapp.Data.Models.UserInfo

class Repository(val firebaseRepository : FirebaseRepository) {

    suspend fun getAllUsersFromFirebase() : ArrayList<UserInfo> {
        return firebaseRepository.getAllUsers()
    }

}