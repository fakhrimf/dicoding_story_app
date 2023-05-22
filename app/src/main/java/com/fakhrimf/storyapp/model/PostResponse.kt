package com.fakhrimf.storyapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostResponse (
    @SerializedName("error")
    var error: Boolean,
    @SerializedName("message")
    var message: String,
    @SerializedName("loginResult")
    var loginResult: LoginResult
)

@Parcelize
data class LoginResult (
    @SerializedName("userId")
    var userId: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("token")
    var token: String,
) : Parcelable