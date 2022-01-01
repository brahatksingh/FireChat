package com.brahatksingh.firechatapp.UI.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brahatksingh.firechatapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [NewMessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewMessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_message, container, false)
    }

}