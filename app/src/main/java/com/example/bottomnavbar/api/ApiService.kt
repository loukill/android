package com.example.bottomnavbar.api

import com.example.bottomnavbar.model.AudioResponse
import com.example.bottomnavbar.model.Category
import com.example.bottomnavbar.model.Score
import com.example.bottomnavbar.model.Text
import com.example.bottomnavbar.model.TextCategory
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/category") // Adjust the endpoint as per your backend API
    suspend fun getCategories(): Response<List<Category>>

    @GET("/txtCategory")
    suspend fun getTextCategories(): Response<List<TextCategory>>

    @GET("/text/synthese/parCategorie/{categoryId}")
    suspend fun getAudioUrlByCategoryId(@Path("categoryId") categoryId: String): Response<AudioResponse>

    @GET("/text/byCategory/{categoryId}")
    suspend fun getTextByCategoryId(@Path("categoryId") categoryId: String): Response<Text>

    @POST("/score")
    suspend fun postScore(@Body score: Score): Response<ResponseBody>

}
