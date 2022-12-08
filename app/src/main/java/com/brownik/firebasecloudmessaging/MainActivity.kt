package com.brownik.firebasecloudmessaging

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.brownik.firebasecloudmessaging.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val messagingService: MessagingService by lazy { MessagingService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        messagingService

        addOnClickListener()
    }

    private fun addOnClickListener() = with(binding) {
        sendButton.setOnClickListener {
            getToken()
                .onEach { token ->
                    val message = MessageData(token, MessageData.MessageInfo(
                        messageTitle.text.toString(),
                        messageContent.text.toString()
                    ))
                    sendMessage(message)
                }
                .catch { Log.d("qwe123", "getToken().catch: ${it.message}") }
                .launchIn(CoroutineScope(Dispatchers.Main))
        }
    }

    @SuppressLint("ResourceType")
    private fun getToken(): Flow<String> = flow {
        var token = ""
        val job = CoroutineScope(Dispatchers.IO).launch {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                token = task.result
            })
        }
        job.join()
        emit(token)
    }

    private suspend fun sendMessage(message: MessageData) {
        RetrofitInstance.api.sendMessage(message)
    }
}