package com.timmymike.json_server_android_assignment.mvvm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.tools.BaseSharePreference
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.util.*

/**======== View Model ========*/

class SplashViewModel(private val context: Context) : ViewModel() {
    val TAG = javaClass.simpleName
    val liveLoadingInterrupt: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() } // According this value To Show now Status
    var urlString: String
        get() = BaseSharePreference.getURLLink(context)
        set(value) = BaseSharePreference.setURLLink(context, value)

    init {

        getData()
    }

    private val waitAPIDuration by lazy {
        context.resources.getInteger(R.integer.wait_api_duration).toLong()
    }


    fun getData() {
        liveLoadingInterrupt.postValue(false)
        val pgDialg: ProgressDialog = ProgressDialog(context).apply {
            needClose = true
            binding.ivClose.setOnClickListener {
                dialog.dismiss()
                liveLoadingInterrupt.postValue(true)
            }
        }

        GlobalScope.launch {

            GlobalScope.launch(Dispatchers.Main) {
                if (!pgDialg.isShowing())
                    pgDialg.show()
            }
            val startTime = Date().time

            try {
                val error = getUserData()
                if (error != null) {
                    loge(TAG, "API ERROR! this is message：${error.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(startTime.getWaitInterval(waitAPIDuration))

            GlobalScope.launch(Dispatchers.Main) {
                if (pgDialg.isShowing()) {
                    pgDialg.dismiss()
                }
            }
        }
    }

    /**It's exactly waitAPIDuration milliseconds*/
    private fun Long.getWaitInterval(duration: Long) = duration - (Date().time - this)

    /**Get User Data*/
    @Throws(Exception::class)
    private fun getUserData(): ResponseBody? {
        val cell = ApiConnect.getService(context).getData()
        val response = cell.execute()
        return if (response.isSuccessful) {
            logi(TAG, response.body() ?: "no data")
            null
        } else {
            response.errorBody()
        }
    }
}

class ViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

