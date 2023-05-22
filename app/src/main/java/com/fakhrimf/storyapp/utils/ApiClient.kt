package com.fakhrimf.storyapp.utils

import com.fakhrimf.storyapp.model.PostResponse
import com.fakhrimf.storyapp.model.StoryResponse
import com.fakhrimf.storyapp.model.UserModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        fun getClient(): Retrofit {
            val client = OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder().baseUrl(API_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}

interface ApiInterface {
    //    @FormUrlEncoded
    @POST("register")
    fun register(
        @Body
        user: UserModel
    ): Call<PostResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email")
        email: String,
        @Field("password")
        password: String
    ): Call<PostResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization")
        token: String
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization")
        token: String,
//        @Header("Content-Type")
//        type: String,
        @Part("description")
        description: RequestBody,
        @Part
        file: MultipartBody.Part,
    ) : Call<PostResponse>
}