package com.brahatksingh.firechatapp.UI.Fragments.NewMessage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brahatksingh.firechatapp.Data.Models.UserInfo
import com.brahatksingh.firechatapp.Data.Repository

class NewMessageViewModel(val userUID : String) : ViewModel() {
    private var repository : Repository

    init {
        repository = Repository
    }

    private var _list = MutableLiveData<ArrayList<UserInfo>>()
    val list : LiveData<ArrayList<UserInfo>>
    get() = _list

    suspend fun getAllUser() {
        Log.d("NEW MESSAGE VIEW MODEL :: ","CALLED GET ALL USERS FUNCTION" )
        if(list.value != null && list.value!!.size > 1) {
            return
        }
        Log.d("NEW MESSAGE VIEW MODEL :: ","CALLED REPOSITORY" )
        _list.value = repository.getAllUsersFromFirebase()
    }

}