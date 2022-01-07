package com.brahatksingh.firechatapp.UI.Fragments.Chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.brahatksingh.firechatapp.Adapters.ChatMessagesAdapter
import com.brahatksingh.firechatapp.Data.Repository
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.UI.Fragments.NewMessage.NewMessageViewModel
import com.brahatksingh.firechatapp.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private val args : ChatFragmentArgs by navArgs()
    private val repository = Repository
    private lateinit var spUserImageView : ImageView
    private lateinit var viewModel: ChatFragmentViewModel
    private lateinit var firebaseAuth : FirebaseAuth
    private val TAG = "CHAT MESSAGE FRAGMENT :: "
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater,container,false)
        // Set Observers Here if needed
        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(ChatFragmentViewModel::class.java)
        viewModel.list.observe(viewLifecycleOwner,Observer { newlist ->
            Log.d(TAG,"LIST CHANGED TO : $newlist")
        })
        binding.caBtnSend.setOnClickListener {
            val message = binding.caEtvNewmessage.text.toString()
            if(message.isEmpty()) {
                return@setOnClickListener
            }
            lifecycleScope.launch {
                viewModel.sendMessage(message,firebaseAuth.currentUser!!.uid,args.spUid)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spUserImageView = requireActivity().findViewById(R.id.toobar_user_image)
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d(TAG,"${repository.getAllChatMessagesFromFirebase(firebaseAuth.currentUser!!.uid,args.spUid)}")
            viewModel.attachListener(firebaseAuth.currentUser!!.uid,args.spUid)
        }
    }

    override fun onPause() {
        super.onPause()
        spUserImageView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        spUserImageView.visibility = View.VISIBLE
    }
}