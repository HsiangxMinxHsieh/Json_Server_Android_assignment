package com.timmymike.json_server_android_assignment.api

import com.google.gson.JsonObject
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @GET("users")
    fun getData(): Call<UserModelData>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("users")
    fun uploadData(@Body jsonObject: JsonObject): Call<UserModelData.UserModelItem>

}