package com.brahatksingh.firechatapp.UI.Fragments.RecentChats

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.UI.Activities.LogInActivity
import com.brahatksingh.firechatapp.UI.Fragments.NewMessage.NewMessageViewModel
import com.brahatksingh.firechatapp.UI.Fragments.NewMessage.NewMessageViewModelFactory
import com.brahatksingh.firechatapp.databinding.FragmentChatBinding
import com.brahatksingh.firechatapp.databinding.FragmentNewMessageBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_recent_chats.*

/**
 * A simple [Fragment] subclass.
 * Use the [RecentChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentChatsFragment : Fragment(R.layout.fragment_recent_chats) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signout_fragment.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),LogInActivity::class.java))
            requireActivity().finish()
        }
    }
}