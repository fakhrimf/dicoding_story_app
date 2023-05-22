package com.fakhrimf.storyapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fakhrimf.storyapp.model.LoginResult
import com.fakhrimf.storyapp.model.PostModel
import com.fakhrimf.storyapp.model.StoryResponse
import com.fakhrimf.storyapp.utils.ApiClient
import com.fakhrimf.storyapp.utils.ApiInterface
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val apiInterface: ApiInterface by lazy {
        ApiClient.getClient().create(ApiInterface::class.java)
    }

    fun getStories(auth: String): MutableLiveData<StoryResponse> {
        val loginResult = Gson().fromJson(auth, LoginResult::class.java)
        val call: Call<StoryResponse> = apiInterface.getStories("Bearer " + loginResult.token)
        val resp = MutableLiveData<StoryResponse>()
        call.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                Log.d("HOME", "onResponse: $response ${loginResult.token} $loginResult")
                if (response.code() == 200) {
                    resp.value = response.body()
                } else {
                    resp.value = StoryResponse(true)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                call.cancel()
            }

        })
        return resp
    }
}