package com.example.bottomnavbar.api

import com.example.bottomnavbar.model.AudioResponse
import com.example.bottomnavbar.model.Category
import com.example.bottomnavbar.model.Score
import com.example.bottomnavbar.model.ScoreTicTac
import com.example.bottomnavbar.model.Text
import com.example.bottomnavbar.model.TextCategory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/category") // Adjust the endpoint as per your backend API
    suspend fun getCategories(): Response<List<Category>>

    @GET("/text")
    suspend fun getAllText(): Response<List<Text>>

    @GET("/text/synthese/parTexte/{textId}")
    suspend fun getAudioUrlByTextId(@Path("textId") textId: String): Response<AudioResponse>

    @GET("/text/{textId}")
    suspend fun getTextById(@Path("textId") textId: String): Response<Text>

    @POST("/score")
    suspend fun postScore(@Body score: Score): Response<ResponseBody>
    @POST("/text/{textId}/consulter")
    suspend fun enregistrerConsultation(@Path("textId") textId: String): Response<ResponseBody>
    @POST("/ticTac")
    fun addScore(@Body scoreRequest: ScoreTicTac): Call<ScoreTicTac>

}
