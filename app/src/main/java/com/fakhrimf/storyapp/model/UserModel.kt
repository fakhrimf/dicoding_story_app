package com.fakhrimf.storyapp.model

import com.google.gson.annotations.SerializedName

data class UserModel (
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
)