package com.brownik.firebasecloudmessaging

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageInterface {
    @POST("fcm/send")
    suspend fun sendMessage(
        @Body message: MessageData,
    ): Response<MessageData>
}