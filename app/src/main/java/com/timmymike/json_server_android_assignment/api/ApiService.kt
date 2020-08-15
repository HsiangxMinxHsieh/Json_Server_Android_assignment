package com.timmymike.json_server_android_assignment.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET
    fun get(@Url url: String): Call<Any>
}