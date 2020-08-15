package com.timmymike.json_server_android_assignment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.util.*

class SplashSettingActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val context: Context = this
    private val activity = this
    private val pgDialg: ProgressDialog by lazy {
        ProgressDialog(context).apply {
            needClose = true
            binding.ivClose.setOnClickListener {
                dialog.dismiss()
                

            }
        }
    }

    val waitAPIDuration by lazy { context.resources.getInteger(R.integer.wait_api_duration).toLong() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_setting)

        GlobalScope.launch {
            GlobalScope.launch(Dispatchers.Main) {
                if (!pgDialg.isShowing() && !activity.isDestroyed)
                    pgDialg.show()
            }
            val startTime = Date().time
            try {
                val error = getUserData()
                if (error != null) {
                    loge(TAG, error.string().toString())

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(startTime.getWaitInterval())

            GlobalScope.launch(Dispatchers.Main) {
                if (pgDialg.isShowing() && !activity.isFinishing)
                    pgDialg.dismiss()
            }
        }
    }

    /**It's exactly waitAPIDuration seconds*/
    private fun Long.getWaitInterval() = waitAPIDuration - (Date().time - this)

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
