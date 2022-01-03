package com.brahatksingh.firechatapp.UI.Fragments.NewMessage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brahatksingh.firechatapp.databinding.FragmentNewMessageBinding

class NewMessageFragment : Fragment() {
    private lateinit var args : NewMessageFragmentArgs
    private lateinit var binding: FragmentNewMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewMessageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}