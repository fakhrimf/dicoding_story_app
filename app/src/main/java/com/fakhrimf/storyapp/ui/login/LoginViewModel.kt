package com.fakhrimf.storyapp.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fakhrimf.storyapp.model.PostResponse
import com.fakhrimf.storyapp.model.UserModel
import com.fakhrimf.storyapp.utils.ApiClient
import com.fakhrimf.storyapp.utils.ApiInterface
import com.fakhrimf.storyapp.utils.LOGIN_KEY
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val apiInterface: ApiInterface by lazy {
        ApiClient.getClient().create(ApiInterface::class.java)
    }

    fun login(
        model: UserModel,
        sharedPreferences: SharedPreferences
    ): MutableLiveData<PostResponse> {
        val call: Call<PostResponse> = apiInterface.login(model.email, model.password)
        Log.d("WHY", "login: $model")
        val resp = MutableLiveData<PostResponse>()
        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                resp.value = response.body()
                Log.d("WORKFFS", "onResponse: ${response.body()}")
                if (response.body() != null) {
                    sharedPreferences.edit()
                        .putString(LOGIN_KEY, Gson().toJson(response.body()!!.loginResult)).apply()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                call.cancel()
                Log.d("KEKW", "onFailure: $t")
            }
        })
        return resp
    }
}