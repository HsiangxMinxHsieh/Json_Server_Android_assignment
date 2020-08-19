package com.timmymike.json_server_android_assignment.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.BaseSharePreference
import com.timmymike.json_server_android_assignment.tools.getWaitInterval
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**======== View Model ========*/

class SplashViewModel(private val context: Application) : AndroidViewModel(context) {
    val TAG = javaClass.simpleName
    val liveLoadingInterrupt: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() } // According this value To Show now Status
    val livePgDialogNeedShow by lazy { MutableLiveData<Boolean>() }
    val liveGetData by lazy { MutableLiveData<UserModelData>() }
    var urlString: String
        get() = BaseSharePreference.getURLLink(context)
        set(value) = BaseSharePreference.setURLLink(context, value)

    init {

        getDataFromAPI()
    }

    private val waitAPIDuration by lazy {
        context.resources.getInteger(R.integer.wait_api_duration).toLong()
    }

    var job: Job? = null

    fun getDataFromAPI() {

        liveLoadingInterrupt.postValue(false)

        job = viewModelScope.launch(Dispatchers.IO) {

            livePgDialogNeedShow.postValue(true)
            val startTime = Date().time
            var data: UserModelData? = null
            try {

                data = getUserData()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(startTime.getWaitInterval(waitAPIDuration))
            liveGetData.postValue(data)
            livePgDialogNeedShow.postValue(false)

        }
    }

    /**Get User Data*/
    @Throws(Exception::class)
    private fun getUserData(): UserModelData? {
        val cell = ApiConnect.getService(context).getData(BaseSharePreference.getURLLink(context))
        val response = cell.execute()
        logi(TAG, "getUserData response is ===>$response")
        return if (response.isSuccessful) {
//            logi(TAG, response.body() ?: "no data")
            response.body()
        } else {
            loge(TAG, "API ERROR! this is error message：${response.errorBody()?.string()}")
            null
        }
    }
}


