package com.brahatksingh.firechatapp.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.brahatksingh.firechatapp.Data.Models.RecentChatData

@Database(entities = [RecentChatData::class],version = 1,exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun getDAO() : ChatDao

    companion object {
        @Volatile
        private var INSTANCE : ChatDatabase? = null

        fun getChatDatabase(context : Context) : ChatDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context,ChatDatabase::class.java,"chat_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

/*
@Database(entities = [ToDoData::class],version = 1,exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun getToDoDao(): ToDoDAO

    companion object {
        @Volatile
        private var INSTANCE : ToDoDatabase? = null

        fun getToDoDatabase(context : Context ) : ToDoDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context,ToDoDatabase::class.java,"todo_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }


}

*/