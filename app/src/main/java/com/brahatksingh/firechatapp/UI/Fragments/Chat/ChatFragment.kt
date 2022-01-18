package com.brahatksingh.firechatapp.UI.Fragments.Chat

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brahatksingh.firechatapp.Adapters.ChatMessagesAdapter
import com.brahatksingh.firechatapp.Data.Models.ChatMessage
import com.brahatksingh.firechatapp.Data.Repository
import com.brahatksingh.firechatapp.R
import com.brahatksingh.firechatapp.databinding.FragmentChatBinding
import com.bumptech.glide.Glide
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
    private lateinit var chatAdapter : ChatMessagesAdapter
    private val TAG = "CHAT MESSAGE FRAGMENT"
    private var spID : Long = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(layoutInflater,container,false)

        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(ChatFragmentViewModel::class.java)

        binding.caBtnSend.setOnClickListener {
            val message = binding.caEtvNewmessage.text.toString()
            if(message.isEmpty()) {
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.sendMessage(message,firebaseAuth.currentUser!!.uid,args.spUid)

//                viewModel.updateLastMessage(args.spIdInDb,message,args.spName,args.spPicurl,args.spUid)
            }
        }
        spID = args.spIdInDb
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "${args.spName}"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageOnToolbar()
        settingViewModel()

        chatAdapter = ChatMessagesAdapter(requireContext(),firebaseAuth.currentUser!!.uid,viewModel.list.value)
        binding.caRvMessages.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        binding.caRvMessages.smoothScrollToPosition(chatAdapter.getLastPosition())

        observeMessageList()

    }

    private fun observeMessageList() {

        viewModel.flag.observe(viewLifecycleOwner, Observer {
            chatAdapter.updateData(viewModel.list.value)
            Log.d(TAG,"Observed that Flag was changed $spID")
            binding.caRvMessages.smoothScrollToPosition(chatAdapter.getLastPosition())
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.findUserInDB(args.spName,args.spPicurl,args.spUid,chatAdapter.getLastMessage())
            }
        })

    }

    private fun settingViewModel() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d(TAG,"${repository.getAllChatMessagesFromFirebase(firebaseAuth.currentUser!!.uid,args.spUid)}")
            viewModel.attachListener(firebaseAuth.currentUser!!.uid,args.spUid)

        }
    }

    private fun setImageOnToolbar() {
        spUserImageView = requireActivity().findViewById(R.id.toobar_user_image)
        Glide.with(requireContext()).load(args.spPicurl).into(spUserImageView)
    }

}