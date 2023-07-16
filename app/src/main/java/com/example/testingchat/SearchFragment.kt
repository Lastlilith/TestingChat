package com.example.testingchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")

    private val viewModel: SearchUserViewModel by viewModels()

    private lateinit var searchUsersAdapter: SearchUserAdapter
    private lateinit var rvUsers: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatFragment = ChatFragment()

        rvUsers = binding.searchUserRecycler
        rvUsers.layoutManager = LinearLayoutManager(requireContext())
        searchUsersAdapter = SearchUserAdapter(requireContext())
        rvUsers.adapter = searchUsersAdapter

        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.userlistProgressBar.visibility = View.VISIBLE
            } else {
                binding.userlistProgressBar.visibility = View.GONE
            }
        }

        viewModel.userList.observe(viewLifecycleOwner) { userList ->
            searchUsersAdapter.setContactList(userList, viewModel.myId!!)
        }
        viewModel.loadAllUsers()


        binding.btnBack.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.main_frame_layout, chatFragment)
                ?.commit()
        }
    }
}