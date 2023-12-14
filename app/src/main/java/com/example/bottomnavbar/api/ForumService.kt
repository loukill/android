package com.example.bottomnavbar.api

import com.example.bottomnavbar.model.Forum
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ForumService {
    @GET("/forums")
    suspend fun getAllForums(): Response<List<Forum>>


    @Multipart
    @POST("votre_endpoint_pour_ajouter_le_forum")
    suspend fun addForum(
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): Response<String>

    @DELETE("/forums/{forumId}")
    suspend fun deleteForum(@Path("forumId") forumId: String): Response<Unit>


    @PUT("/forums/{forumId}")
    suspend fun updateForum(@Path("forumId") forumId: String, @Body updatedForum: Forum): Response<String>
}