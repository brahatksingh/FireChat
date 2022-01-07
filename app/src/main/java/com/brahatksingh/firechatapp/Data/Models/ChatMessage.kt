package com.brahatksingh.firechatapp.Data.Models

data class ChatMessage(val fromUID : String,
                       val forUID : String, val messageText : String,
                       val isImage : String,
                       val timeStamp : String,
                       val imageUrl : String) {
    constructor() : this("","","","","","")
}