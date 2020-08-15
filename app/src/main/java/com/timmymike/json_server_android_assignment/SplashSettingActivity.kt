package com.timmymike.json_server_android_assignment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.logi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class SplashSettingActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val context: Context = this
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_setting)

        GlobalScope.launch {
            try {
                val error = getUserData()
                if (error != null) {
                    loge(TAG, error.string().toString())

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**Get User Data*/
    @Throws(Exception::class)
    private fun getUserData(): ResponseBody? {
        val cell = ApiConnect.getService(activity).getData()
        val response = cell.execute()
        return if (response.isSuccessful) {
            logi(TAG, response.body() ?: "no data")
            null
        } else {
            response.errorBody()
        }
    }
}
