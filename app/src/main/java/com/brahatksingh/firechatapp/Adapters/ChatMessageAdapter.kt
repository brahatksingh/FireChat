package com.brahatksingh.firechatapp.Adapters

import android.app.AlertDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brahatksingh.firechatapp.Data.Models.ChatMessage
import com.brahatksingh.firechatapp.R
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class ChatMessagesAdapter(val context : Context, val userID : String, var messageList : ArrayList<ChatMessage>?) : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessagesViewHolder {
        when(viewType) {
            1-> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_imagemessage_fromuser,parent,false)
                return ChatMessagesViewHolder(view)

            }
            2-> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_message_fromuser,parent,false)
                return ChatMessagesViewHolder(view)
            }
            3->{
                val view = LayoutInflater.from(context).inflate(R.layout.item_imagemessage_foruser,parent,false)
                return ChatMessagesViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_message_foruser,parent,false)
                return ChatMessagesViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ChatMessagesViewHolder, position: Int) {
        when(getItemViewType(position)) {
            1 ->{
                Glide.with(context).load(messageList!![position].imageUrl).into(holder.image_fromUser).onLoadFailed(
                    ContextCompat.getDrawable(context,R.drawable.defaultimageforchat))
            }
            2-> {
                holder.messageText_fromUser.text = messageList!![position].messageText
            }
            3->{
                Glide.with(context).load(messageList!![position].imageUrl).into(holder.image_forUser).onLoadFailed(
                    ContextCompat.getDrawable(context,R.drawable.defaultimageforchat))
            }
            else ->{
                holder.messageText_forUser.text = messageList!![position].messageText
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList!!.size
    }

    inner class ChatMessagesViewHolder(val itemView : View) : RecyclerView.ViewHolder(itemView) {
        val messageText_fromUser = itemView.findViewById<TextView>(R.id.item_rvca_fromUser_textView)
        val messageText_forUser = itemView.findViewById<TextView>(R.id.item_rvca_forUser_textView)
        val image_fromUser = itemView.findViewById<ImageView>(R.id.item_rvca_fromUser_imageView)
        val image_forUser = itemView.findViewById<ImageView>(R.id.item_rvca_forUser_imageView)
    }

    override fun getItemViewType(position: Int): Int {
        return when(messageList!![position].fromUID) {
            userID -> {
                if(messageList!![position].image.equals("1")) {
                    return 1
                }
                return 2
            }
            else -> {
                if(messageList!![position].image.equals("1")) {
                    return 3
                }
                return 4
            }
        }
    }

    fun getLastPosition() : Int {
        return if (messageList!!.isEmpty()) 0 else messageList!!.size - 1
    }

    fun updateData(list : ArrayList<ChatMessage>?) {
        messageList = list
        notifyDataSetChanged()
    }

    fun getLastMessage() : String {
        return if(messageList!!.isEmpty()) {
            return "DEF VALUE AS SIZE IS INVALID"
        }
        else {
            if(messageList!![getLastPosition()].image.equals("1")) {
                return "Image"
            }
            return messageList!![getLastPosition()].messageText
        }
    }

}