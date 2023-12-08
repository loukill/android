package com.example.bottomnavbar.api

import com.example.bottomnavbar.model.Category
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/category") // Adjust the endpoint as per your backend API
    suspend fun getCategories(): Response<List<Category>>
}
