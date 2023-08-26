package ikhwan.dicoding.storyapp.network

import ikhwan.dicoding.storyapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

    @POST("/v1/login")
    fun login(
        @Body loginBody: LoginBody
    ): Call<LoginResponse>

    @POST("/v1/register")
    fun register(
        @Body registerBody: RegisterBody
    ): Call<FileUploadResponse>

    @GET("/v1/stories")
    fun getAllStories(
        @Header("Authorization") auth: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null,
    ): Call<StoriesResponse>
}