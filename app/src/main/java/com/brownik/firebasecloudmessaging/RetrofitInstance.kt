package com.brownik.firebasecloudmessaging

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.FCM_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MessageInterface by lazy {
        retrofit.create(MessageInterface::class.java)
    }

    private fun provideOkHttpClient(
        interceptor: AppInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    // 인증 토큰 필요로 인한 헤더
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "key=${Constants.FCM_KEY}")
                .addHeader("Content-Type", "application/json")
                .build()
            proceed(newRequest)
        }
    }
}