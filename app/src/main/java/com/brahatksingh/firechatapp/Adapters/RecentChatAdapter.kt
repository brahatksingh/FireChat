package com.brahatksingh.firechatapp.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.brahatksingh.firechatapp.Data.Models.RecentChatData
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.UI.Fragments.NewMessage.NewMessageFragmentDirections
import com.brahatksingh.firechatapp.UI.Fragments.RecentChats.RecentChatsFragmentDirections
import com.bumptech.glide.Glide
import org.xmlpull.v1.XmlPullParser

class RecentChatAdapter(val gotContext : Context,val navController: NavController) : RecyclerView.Adapter<RecentChatAdapter.RecentChatViewHolder>() {

    var list : ArrayList<RecentChatData>? = ArrayList<RecentChatData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatViewHolder {
        return RecentChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recent_message,parent,false))
    }

    override fun onBindViewHolder(holder: RecentChatViewHolder, position: Int) {
        holder.spname.setText(list!![position].sp_name)
        holder.splastmessage.setText(list!![position].message)
        Glide.with(gotContext).load(list!![position].sp_picURL).into(holder.spimv).onLoadFailed(ContextCompat.getDrawable(gotContext,R.drawable.profilepicnormall))
        holder.entireLayout.setOnClickListener {
            val action = RecentChatsFragmentDirections.actionRecentChatsFragmentToChatFragment(list!![position].sp_name,list!![position].sp_picURL,list!![position].sp_uid)
            navController.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun updateList(gotList : ArrayList<RecentChatData>?) {
        list = gotList
        notifyDataSetChanged()
    }

    inner class RecentChatViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val spimv = itemView.findViewById<ImageView>(R.id.item_rvrcf_image)
        val spname = itemView.findViewById<TextView>(R.id.item_rvrcf_username)
        val splastmessage = itemView.findViewById<TextView>(R.id.item_rvrcf_message)
        val entireLayout = itemView
    }

}