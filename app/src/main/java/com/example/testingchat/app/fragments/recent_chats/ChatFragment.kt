package com.example.testingchat.app.fragments.recent_chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testingchat.R
import com.example.testingchat.app.fragments.new_chats.SearchFragment
import com.example.testingchat.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding ?: throw RuntimeException("FragmentChatBinding == null")

    private lateinit var searchContactsFragment: SearchFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchContactsFragment = SearchFragment()

        binding.btnNewChat.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.main_frame_layout, searchContactsFragment)?.commit()
        }
    }

}