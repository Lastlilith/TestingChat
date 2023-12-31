package com.example.testingchat.app.fragments.profile

import android.app.Activity
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
import com.example.testingchat.app.activities.splash.SplashActivity
import com.example.testingchat.databinding.FragmentProfileBinding
import com.example.testingchat.model.UserModel
import com.example.testingchat.utils.AndroidUtil
import com.example.testingchat.utils.FirebaseUtil
import com.example.testingchat.utils.FirebaseUtil.currentUserDetails
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.messaging.FirebaseMessaging


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")
    private lateinit var imagePickLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectedImageUri: Uri
    private lateinit var profilePicture: ImageView
    private lateinit var currentUserModel: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserData()

        profilePicture = binding.ivProfilePic
        imagePickLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result!!.data!!.data!!
                AndroidUtil.setProfileImage(requireContext(), selectedImageUri, profilePicture)
            }
        }

        binding.btnEditPhoto.setOnClickListener {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                .createIntent {
                    imagePickLauncher.launch(it)
                }
        }

        binding.btnLogout.setOnClickListener {
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener {
                if (it.isSuccessful) {
                    FirebaseUtil.logout()
                    val intent = Intent(requireContext(), SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        binding.btnUpdateProfile.setOnClickListener {
            setInProgress(true)
            if (selectedImageUri != null) {
                FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener {
                        updateToFirestore()
                    }
            } else {
                updateToFirestore()
            }
        }

    }

    private fun getUserData() {
        setInProgress(true)
        FirebaseUtil.getCurrentProfilePicStorageRef().downloadUrl
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uri: Uri = task.result
                    AndroidUtil.setProfileImage(requireContext(), uri, binding.ivProfilePic)
                }
            }
        currentUserDetails().get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
            setInProgress(false)
            currentUserModel = task.result.toObject(UserModel::class.java)!!
            binding.tvPhone.text = currentUserModel.phone
            binding.tvNickname.text = currentUserModel.username

        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogout.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnLogout.isEnabled = true
        }
    }



    private fun updateToFirestore() {
        currentUserDetails().set(currentUserModel)
            .addOnCompleteListener { task: Task<Void?> ->
                setInProgress(false)
                if (task.isSuccessful) {
                    AndroidUtil.showToast(context, "Updated successfully")
                } else {
                    AndroidUtil.showToast(context, "Updated failed")
                }
            }
    }

}