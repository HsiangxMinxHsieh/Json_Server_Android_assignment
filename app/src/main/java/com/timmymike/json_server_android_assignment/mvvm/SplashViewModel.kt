package com.timmymike.json_server_android_assignment.mvvm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.LoginActivity
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.BaseSharePreference
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.getWaitInterval
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
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
            var data: UserModelData? = null
            try {
                data = getUserData()
                if (data == null) {
                    loge(TAG, "getUserData do not get the data.")
                    return@launch
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(startTime.getWaitInterval(waitAPIDuration))
            //To Login
            loge(TAG,"放進去前，data是===>$data")
            val intent = Intent(context, LoginActivity::class.java)
            intent.putParcelableArrayListExtra(LoginActivity.KEY_USER_DATA, data )
            (context as? Activity)?.startActivity(intent)

            GlobalScope.launch(Dispatchers.Main) {
                if (pgDialg.isShowing() && (context as? Activity)?.isFinishing == false) {
                    pgDialg.dismiss()
                }
            }
        }
    }


    /**Get User Data*/
    @Throws(Exception::class)
    private fun getUserData(): UserModelData? {
        val cell = ApiConnect.getService(context).getData()
        val response = cell.execute()
        return if (response.isSuccessful) {
//            logi(TAG, response.body() ?: "no data")
            response.body()
        } else {
            loge(TAG, "API ERROR! this is error message：${response.errorBody()?.string()}")
            null
        }
    }
}

class ViewModelSplashFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
