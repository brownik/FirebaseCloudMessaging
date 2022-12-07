package com.brownik.firebasecloudmessaging

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brownik.firebasecloudmessaging.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val messagingService: MessagingService by lazy { MessagingService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        messagingService
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("ResourceType")
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if(!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result
        })
    }
}