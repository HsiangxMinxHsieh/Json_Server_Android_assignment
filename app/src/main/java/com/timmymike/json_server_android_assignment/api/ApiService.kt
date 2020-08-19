package com.timmymike.json_server_android_assignment.api

import com.google.gson.JsonObject
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET
    fun getData(@Url url :String): Call<UserModelData>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    fun uploadData(@Url url :String,@Body jsonObject: JsonObject): Call<UserModelData.UserModelItem>

}