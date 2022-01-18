package com.brahatksingh.firechatapp.Data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.brahatksingh.firechatapp.Data.Models.RecentChatData

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat_table ORDER BY id ASC")
    fun getAllChats() : List<RecentChatData>

    @Insert
    suspend fun addChat(newchat: RecentChatData) : Long

    @Update
    suspend fun updateChat(newchat : RecentChatData)

    @Delete
    suspend fun deleteData(chat : RecentChatData)

    @Query("UPDATE chat_table SET message = :lastMessage WHERE id = :gotID")
    suspend fun updateWithChatID(gotID : Long,lastMessage : String)

    @Query("SELECT id FROM chat_table WHERE sp_uid = :gotUID ")
    suspend fun searchWithUID(gotUID : String) : Long

}