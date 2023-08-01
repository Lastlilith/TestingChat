package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.testingchat.databinding.ActivityRegisterUserBinding
import com.example.testingchat.model.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding

    private val viewModel: RegisterUserViewModel by viewModels()
    private lateinit var userModel: UserModel
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        phoneNumber = intent.extras?.getString("phone").toString()
        val formattedNumber = formatPhoneNumber(phoneNumber)
        binding.tvPhone.text = formattedNumber

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val filteredStringBuilder = StringBuilder()
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
            val pattern = "[A-Za-z0-9-_]+".toRegex()

            for (i in start until end) {
                val character = source[i]
                if (allowedChars.contains(character) && pattern.matches(filteredStringBuilder.toString() + character)) {
                    filteredStringBuilder.append(character)
                }
            }
            filteredStringBuilder.toString()
        }
        val filters = arrayOf(filter)
        binding.etUsername.filters = filters


        binding.btnLetMeIn.setOnClickListener {
//                Toast.makeText(applicationContext, "Name: ${binding.etName.text}, username: ${binding.etUsername.text}, phone: $phoneNumber", Toast.LENGTH_SHORT).show()
                setUserName()
        }

    }

    private fun setUserName() {
        val name = binding.etName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        if (name.isEmpty() || name.length < 3) {
            binding.etName.error = "Please enter valid input"
            return
        } else if (username.isEmpty() || username.length < 3) {
            binding.etUsername.error = "Please enter valid input"
            return
        } else {
            setInProgress(true)
            userModel = UserModel(phoneNumber, name, username, createdTimeStamp = Timestamp.now())
            currentUserDetails().set(userModel).addOnCompleteListener { task ->
                setInProgress(false)
                if (task.isSuccessful) {
                    val intent = Intent(this@RegisterUserActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Something went wrong, try again later", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun currentUserId(): String {
        return FirebaseAuth.getInstance().uid!!
    }

    private fun currentUserDetails(): DocumentReference {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId())
    }


    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.usernameProgressBar.visibility = View.VISIBLE
            binding.btnLetMeIn.isEnabled = false
        } else {
            binding.usernameProgressBar.visibility = View.GONE
            binding.btnLetMeIn.isEnabled = true
        }
    }


    private fun formatPhoneNumber(phoneNumber: String): String {
        val digitsOnly = phoneNumber.replace("\\D".toRegex(), "")

        val formattedNumber = StringBuilder("+")
        var currentIndex = 0

        if (currentIndex < digitsOnly.length) {
            formattedNumber.append(digitsOnly.substring(currentIndex, currentIndex + 1))
            currentIndex++
        }

        formattedNumber.append(" (")

        if (currentIndex + 3 <= digitsOnly.length) {
            formattedNumber.append(digitsOnly.substring(currentIndex, currentIndex + 3))
            currentIndex += 3
        }

        formattedNumber.append(") ")
        if (currentIndex + 3 <= digitsOnly.length) {
            formattedNumber.append(digitsOnly.substring(currentIndex, currentIndex + 3))
            currentIndex += 3
        }

        formattedNumber.append("-")
        if (currentIndex < digitsOnly.length) {
            formattedNumber.append(digitsOnly.substring(currentIndex))
        }

        return formattedNumber.toString()
    }

//    private fun observeViewModel() {
//        viewModel.apply {
//            phoneText.observe(this@RegisterUserActivity) {
//                intent.extras?.getString("phoneNumber")
//                Log.e("POPO", "myPhoneNumer: ${intent.extras?.getString("phoneNumber")}")
//            }
//            userNameText.observe(this@RegisterUserActivity) {
//                binding.etUsername.setText(it)
//            }
//            nameText.observe(this@RegisterUserActivity) {
//                binding.etName.setText(it)
//            }
//            progress.observe(this@RegisterUserActivity) {
//                if (it) {
//                    binding.usernameProgressBar.visibility = View.VISIBLE
//                    binding.btnLetMeIn.isEnabled = false
//                } else {
//                    binding.usernameProgressBar.visibility = View.GONE
//                    binding.btnLetMeIn.isEnabled = true
//                }
//            }
//            error.observe(this@RegisterUserActivity) {
//                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
//            }
//            success.observe(this@RegisterUserActivity) {
//                if (it) {
//                    val intent = Intent(this@RegisterUserActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    Animatoo.animateSlideLeft(this@RegisterUserActivity)
//                    finish()
//                }
//            }
//            dataSent.observe(this@RegisterUserActivity) {
//                if (it) {
//                    Snackbar.make(binding.root, "Data sent successfully", Snackbar.LENGTH_LONG).show()
//                } else {
//                    Snackbar.make(binding.root, "Failed to send data", Snackbar.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
}