package com.brahatksingh.firechatapp.UI.Fragments.NewMessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewMessageViewModelFactory(private val userUID : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewMessageViewModel::class.java)){
            return NewMessageViewModel(userUID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}