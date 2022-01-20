package com.brahatksingh.firechatapp.UI.Fragments.RecentChats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brahatksingh.firechatapp.Adapters.RecentChatAdapter
import com.brahatksingh.firechatapp.Data.ChatDatabase
import com.brahatksingh.firechatapp.Data.Models.RecentChatData
import com.brahatksingh.firechatapp.Data.Repository
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.UI.Activities.LogInActivity
import com.brahatksingh.firechatapp.UI.Fragments.NewMessage.NewMessageViewModel
import com.brahatksingh.firechatapp.UI.Fragments.NewMessage.NewMessageViewModelFactory
import com.brahatksingh.firechatapp.databinding.FragmentChatBinding
import com.brahatksingh.firechatapp.databinding.FragmentNewMessageBinding
import com.brahatksingh.firechatapp.databinding.FragmentRecentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_recent_chats.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList


class RecentChatsFragment : Fragment(R.layout.fragment_recent_chats) {

    private lateinit var binding : FragmentRecentChatsBinding
    private lateinit var adapter: RecentChatAdapter
    private lateinit var viewModel : RecentChatViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRecentChatsBinding.inflate(inflater,container,false)
        lifecycleScope.launch {
            val newDAO = ChatDatabase.getChatDatabase(requireContext()).getDAO()
            Repository.instantiateDAOofRepository(newDAO)
        }

        setRecyclerView()
        showDB()
        return binding.root

    }

    private fun showDB() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("DB DATA ////","${Repository.getAllChatsFromDB().value}")
        }
    }

    private fun setRecyclerView() {
        lifecycleScope.launch(Dispatchers.IO) {

            val list = async {
                Repository.getAllChatsFromDB().value
            }.await()
            setAdapterAndLayoutManager(list)
        }
    }

    private fun setAdapterAndLayoutManager(list : ArrayList<RecentChatData>?) {
        lifecycleScope.launch(Dispatchers.Main) {
            adapter = RecentChatAdapter(requireContext(),findNavController())
            adapter.updateList(list)
            binding.rvRcf.adapter = adapter
            binding.rvRcf.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("RECENT CHAT FRAGMENT","&&& ${Repository.getAllChatsFromDB().value}")
        }
    }
}