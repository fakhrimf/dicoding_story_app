package com.fakhrimf.storyapp.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fakhrimf.storyapp.model.PostResponse
import com.fakhrimf.storyapp.model.UserModel
import com.fakhrimf.storyapp.utils.ApiClient
import com.fakhrimf.storyapp.utils.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val apiInterface: ApiInterface by lazy {
        ApiClient.getClient().create(ApiInterface::class.java)
    }

    fun register(userModel: UserModel) : MutableLiveData<PostResponse> {
        val call: Call<PostResponse> = apiInterface.register(userModel)
        val resp = MutableLiveData<PostResponse>()
        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                Log.d("FIND_ME", "onResponse: $response")
                resp.value = response.body()
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                call.cancel()
            }
        })
        return resp
    }
}