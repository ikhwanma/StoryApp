package ikhwan.dicoding.storyapp.model

import com.google.gson.annotations.SerializedName

data class LoginBody(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String
)

data class RegisterBody(
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String
)
