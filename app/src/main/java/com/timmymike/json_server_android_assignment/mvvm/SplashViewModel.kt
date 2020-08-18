package com.timmymike.json_server_android_assignment.mvvm

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timmymike.json_server_android_assignment.LoginActivity
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.BaseSharePreference
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.dialog.showMessageDialogOnlyOKButton
import com.timmymike.json_server_android_assignment.tools.getWaitInterval
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.logi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**======== View Model ========*/

class SplashViewModel(private val application: Application) : ViewModel() {
    val TAG = javaClass.simpleName
    val liveLoadingInterrupt: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() } // According this value To Show now Status
    var urlString: String
        get() = BaseSharePreference.getURLLink(application)
        set(value) = BaseSharePreference.setURLLink(application, value)

    init {

        getDataFromAPI()
    }

    private val waitAPIDuration by lazy {
        application.resources.getInteger(R.integer.wait_api_duration).toLong()
    }


    private var job: Job? = null
    fun getDataFromAPI() {

        liveLoadingInterrupt.postValue(false)
        val pgDialg: ProgressDialog = ProgressDialog(application).apply {
            needClose = true
            binding.ivClose.setOnClickListener {
                dialog.dismiss()
                liveLoadingInterrupt.postValue(true)
                job?.cancel()
            }
        }

        job = viewModelScope.launch(Dispatchers.IO) {

            viewModelScope.launch(Dispatchers.Main) {
                if (!pgDialg.isShowing())
                    pgDialg.show()
            }
            val startTime = Date().time
            var data: UserModelData? = null
            var getDataFail = false
            try {
                data = getUserData()
                if (data == null || data.isEmpty()) {
                    getDataFail  = true
                    loge(TAG, "getUserData do not get the data.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getDataFail = true

            }

            delay(startTime.getWaitInterval(waitAPIDuration))
            viewModelScope.launch(Dispatchers.Main) {
                if (pgDialg.isShowing() && (application as? Activity)?.isFinishing == false) {
                    pgDialg.dismiss()
                }
                if (getDataFail) {
                    showMessageDialogOnlyOKButton(application, application.getString(R.string.error_dialog_title), application.getString(R.string.splash_no_data_get_error_message))
                    liveLoadingInterrupt.postValue(true)
                } else {
                    //To Login
                    val intent = Intent(application, LoginActivity::class.java)
                    intent.putParcelableArrayListExtra(LoginActivity.KEY_USER_DATA, data)
                    (application as? Activity)?.startActivity(intent)
                    (application as? Activity)?.finish()
                }
            }
        }

    }

    /**Get User Data*/
    @Throws(Exception::class)
    private fun getUserData(): UserModelData? {
        val cell = ApiConnect.getService(application).getData()
        val response = cell.execute()
        logi(TAG,"getUserData response is ===>$response")
        return if (response.isSuccessful) {
//            logi(TAG, response.body() ?: "no data")
            response.body()
        } else {
            loge(TAG, "API ERROR! this is error message：${response.errorBody()?.string()}")
            null
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
        loge(TAG, "ViewModelCleared.")
    }
}


