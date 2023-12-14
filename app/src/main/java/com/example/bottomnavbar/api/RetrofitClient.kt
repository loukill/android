package com.example.bottomnavbar.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/" // Replace with your backend URL

    // Logging Interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Authorization Interceptor
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NTRkZjE4YjUzNWVjMDRlZmVkYWJiMGIiLCJ1c2VybmFtZSI6Im1hbGVrIiwiaWF0IjoxNzAxMjcwNTU3LCJleHAiOjE3MDEyNzc3NTd9.TgGqbVMygaL6DFB_EyPylftB-9pRYJWryCEWKZ_Tyw4"
            )
            .method(original.method, original.body)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val forumService: ForumService by lazy {
        retrofit.create(ForumService::class.java)
    }
}

