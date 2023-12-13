package com.example.bottomnavbar.model
import androidx.room.Entity

@Entity
data class User(
    val _id: String,
    val name: String,
    val password:String,
    val lastname : String,
    val resetCode: String,
    val token: String,
    val email:String,
    val numTel: String,
    val role:Roles

) {
    override fun toString(): String {
        return "User(_id='$_id', name='$name', lastname='$lastname', " +
                "resetCode='$resetCode', token='$token', email='$email', " +
                "password='$password', numTel='$numTel', role=$role)"
    }

}
data class User1(
    val numTel: String,
    val password: String
)
data class User2(
    val name: String,
    val numTel: String,
    val password: String


)
data class User3(
    val numTel: String
)
data class User5(
    val name: String,
    val numTel: String,
    val password: String,
    val email:String


)
