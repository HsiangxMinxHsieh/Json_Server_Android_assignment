package com.timmymike.json_server_android_assignment.mvvm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.tools.BaseSharePreference
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.getWaitInterval
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.logi
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import java.util.*

/**======== View Model ========*/

class LoginViewModel(private val context: Context) : ViewModel() {
    val TAG = javaClass.simpleName


    var account = ""
    var password = ""

    var urlString: String
        get() = BaseSharePreference.getURLLink(context)
        set(value) = BaseSharePreference.setURLLink(context, value)

    private val loginDuration by lazy {
        context.resources.getInteger(R.integer.login_duration).toLong()
    }


    fun login() {
//        liveLoadingInterrupt.postValue(false)
        val pgDialg = ProgressDialog(context)

        GlobalScope.launch {

            GlobalScope.launch(Dispatchers.Main) {
                if (!pgDialg.isShowing())
                    pgDialg.show()
            }
            val startTime = Date().time

            try {
//                val error = getUserData()
//                if (error != null) {
//                    loge(TAG, "API ERROR! this is message：${error.string()}")
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(startTime.getWaitInterval(loginDuration))

            GlobalScope.launch(Dispatchers.Main) {
                if (pgDialg.isShowing()) {
                    pgDialg.dismiss()
                }
            }
        }
    }


}

class ViewModelLoginFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

