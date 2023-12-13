package com.example.bottomnavbar.api

import com.example.bottomnavbar.model.User
import com.example.bottomnavbar.model.User1
import com.example.bottomnavbar.model.User2
import com.example.bottomnavbar.model.User3
import com.example.bottomnavbar.model.User5
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface Login {


    @POST("/user/login") // Assurez-vous d'utiliser le bon endpoint sur votre serveur

    fun login(@Body user: User1): Call<User>

    @POST("/user/UtilisateurSignUp") // Assurez-vous d'utiliser le bon endpoint sur votre serveur
    fun register(@Body user: User2): Call<User>
    @POST("/user/forgetPassword")
    fun sendResetCode(@Body user: User3): Call<User>
    @POST("/user/DoctorSignup") // Assurez-vous d'utiliser le bon endpoint sur votre serveur
    fun registerd(@Body user: User5): Call<User>

    @PUT("user/resetPassword")
    fun updateUser2(@Body user: User1) : Call<User>

}