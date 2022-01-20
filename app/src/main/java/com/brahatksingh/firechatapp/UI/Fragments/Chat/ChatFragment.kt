package com.brahatksingh.firechatapp.UI.Fragments.Chat

import android.content.Intent
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
    var IMAGE_RESPONE_CODE = 1
    private val TAG = "CHAT MESSAGE FRAGMENT"

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
            }
        }
        binding.caBtnAddImage.setOnClickListener {
            handleImageClick()
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "${args.spName}"

        return binding.root
    }

    private fun handleImageClick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pic"),IMAGE_RESPONE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) {
            if(data != null) {
                val selectedPhoto = data?.data
                lifecycleScope.launch {
                    viewModel.uploadImage(selectedPhoto!!,args.spUid,firebaseAuth.currentUser!!.uid)
                }
            }
        }
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
            binding.caRvMessages.smoothScrollToPosition(chatAdapter.getLastPosition())
            lifecycleScope.launch(Dispatchers.IO) {
                val lm = chatAdapter.getLastMessage()
                if(!lm.equals("DEF VALUE AS SIZE IS INVALID")) {
                    viewModel.findUserInDB(args.spName,args.spPicurl,args.spUid,lm)
                }
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