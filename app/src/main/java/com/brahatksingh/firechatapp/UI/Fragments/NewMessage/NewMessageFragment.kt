package com.brahatksingh.firechatapp.UI.Fragments.NewMessage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brahatksingh.firechatapp.Adapters.NewMessageAdapter
import com.brahatksingh.firechatapp.Data.Models.UserInfo
import com.brahatksingh.firechatapp.databinding.FragmentNewMessageBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewMessageFragment : Fragment() {
    private lateinit var args : NewMessageFragmentArgs
    private lateinit var viewModel : NewMessageViewModel
    private lateinit var viewModelFactory : NewMessageViewModelFactory
    private lateinit var binding: FragmentNewMessageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapter: NewMessageAdapter
    private val TAG = "NEW MESSAGE FRAGMENT ::"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance()
        binding = FragmentNewMessageBinding.inflate(layoutInflater,container,false)
        viewModelFactory = NewMessageViewModelFactory(firebaseAuth.currentUser!!.uid)
        viewModel = ViewModelProvider(this,viewModelFactory).get(NewMessageViewModel::class.java)


        // Set Observers Here if needed
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG,"BEFORE ASYNC 1")
        lifecycleScope.launch {
            Log.d(TAG,"BEFORE ASYNC 2 ${viewModel.list}")
            async {
                viewModel.getAllUser()
            }.await()
            Log.d(TAG,"AFTER ASYNC 1 ${viewModel.list}")
            Log.d(TAG,"AFTER ASYNC 2")
            binding.pbNmf.visibility = View.GONE
            adapter = NewMessageAdapter(findNavController())
            adapter.updateList(viewModel.list.value ?: ArrayList<UserInfo>())
            binding.rvNewMessageFragment.adapter = adapter
            binding.rvNewMessageFragment.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }

    }

}