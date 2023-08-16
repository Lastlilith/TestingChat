package com.example.testingchat.app.fragments.recent_chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.R
import com.example.testingchat.app.adapters.RecentChatRecyclerAdapter
import com.example.testingchat.app.fragments.new_chats.SearchFragment
import com.example.testingchat.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding ?: throw RuntimeException("FragmentChatBinding == null")

    private lateinit var searchContactsFragment: SearchFragment
    private val viewModel: RecentChatsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecentChatRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvRecentChats

        searchContactsFragment = SearchFragment()

        binding.btnNewChat.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.main_frame_layout, searchContactsFragment)?.commit()
        }
    }

}