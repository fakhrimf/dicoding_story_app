package com.fakhrimf.storyapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.http.Header

@Parcelize
data class PostModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("lat")
    val lat: Float,
    @SerializedName("lon")
    val lon: Float,
) : Parcelable

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("listStory")
    val listStory: ArrayList<PostModel>? = null,
    @Header("Authorization")
    val authHeader: String? = null,
)