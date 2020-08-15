package com.timmymike.json_server_android_assignment.api

import android.content.Context
import com.timmymike.json_server_android_assignment.tools.BaseSharePreference
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConnect {
    const val def_url = "http://10.0.3.2:3000/"
    private var apiService : ApiService? = null

    fun getService(context: Context): ApiService {
        if (apiService == null) {
            apiService = init(context)
        }
        return apiService ?: init(context)
    }

    private fun init(context: Context): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl( BaseSharePreference.getURLLink(context)) // get URL in BaseSharePreference
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}