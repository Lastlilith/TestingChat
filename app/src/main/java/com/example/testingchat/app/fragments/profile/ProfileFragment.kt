package com.example.testingchat.app.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testingchat.app.fragments.edit_profile.EditProfileFragment
import com.example.testingchat.R
import com.example.testingchat.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private lateinit var editProfileFragment: EditProfileFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editProfileFragment = EditProfileFragment()

        binding.btnEdit.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.main_frame_layout, editProfileFragment)?.commit()
        }
    }

}