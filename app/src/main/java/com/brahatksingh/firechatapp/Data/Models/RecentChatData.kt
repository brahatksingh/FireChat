package com.brahatksingh.firechatapp.Data.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table")
data class RecentChatData(@PrimaryKey(autoGenerate = true)var id : Int,
                          var sp_name : String,
                          var message : String,
                          var sp_uid : String,
                          var sp_picURL : String,
                          var isReceived : Boolean = true)
{

}