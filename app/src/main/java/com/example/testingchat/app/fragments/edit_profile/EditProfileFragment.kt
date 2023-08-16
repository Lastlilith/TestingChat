package com.example.testingchat.app.fragments.edit_profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testingchat.R
import com.example.testingchat.app.fragments.profile.ProfileFragment
import com.example.testingchat.databinding.FragmentEditProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding: FragmentEditProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentEditProfileBinding == null")

    private lateinit var imagePickLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectedImageUri: Uri

    private lateinit var profilePicture: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileFragment = ProfileFragment()

        profilePicture = binding.ivProfilePic
        imagePickLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedImageUri = result!!.data!!.data!!
                setProfileImage(selectedImageUri, profilePicture)
            }
        }

        binding.btnDone.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.main_frame_layout, profileFragment)
                ?.commit()
        }

        binding.btnEditPhoto.setOnClickListener {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                .createIntent {
                    imagePickLauncher.launch(it)
                }
        }
    }

    private fun setProfileImage(uri: Uri, image: ImageView) {
        Glide.with(this).load(uri).apply(RequestOptions.circleCropTransform()).into(image)
    }


}