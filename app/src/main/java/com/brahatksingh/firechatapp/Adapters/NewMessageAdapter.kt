package com.brahatksingh.firechatapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.brahatksingh.firechatapp.Data.Models.UserInfo
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.UI.Fragments.NewMessage.NewMessageFragmentDirections
import com.bumptech.glide.Glide

class NewMessageAdapter(val navController: NavController) : RecyclerView.Adapter<NewMessageAdapter.NewMessageViewHolder>() {

    val list = ArrayList<UserInfo>()

    private var data : ArrayList<UserInfo> = ArrayList<UserInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_newmessasgesactivity,parent,false)
        return NewMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewMessageViewHolder, position: Int) {
        holder.username.text = data[position].name
        holder.useremail.text = data[position].email
        Glide.with(holder.entireLayout.context).load(data[position].profilePicUrl).into(holder.userImage).onLoadFailed(ContextCompat.getDrawable(holder.entireLayout.context,R.drawable.profilepicnormall))
        holder.entireLayout.setOnClickListener {
            val action = NewMessageFragmentDirections
                .actionNewMessageFragmentToChatFragment(data[position].name,data[position].profilePicUrl,data[position].userId,-1)
            navController.navigate(action)
            Log.d("NewMessagesActivity","${data[position].toString()}")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateList(userList : ArrayList<UserInfo>) {
        data = userList
        notifyDataSetChanged()
    }

    inner class NewMessageViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.item_rvrcf_username)
        val useremail = itemView.findViewById<TextView>(R.id.item_rvrcf_message)
        val userImage = itemView.findViewById<ImageView>(R.id.item_rvrcf_image)
        val entireLayout = itemView
    }

}