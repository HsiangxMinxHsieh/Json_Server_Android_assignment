package com.timmymike.json_server_android_assignment.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.dialog.TextDialog
import com.timmymike.json_server_android_assignment.tools.dialog.showMessageDialogOnlyOKButton
import com.timmymike.json_server_android_assignment.tools.getWaitInterval
import com.timmymike.json_server_android_assignment.tools.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


/**======== View Model ========*/

class LoginViewModel(private val context: Application, val userArray: ArrayList<UserModelData.UserModelItem>) : AndroidViewModel(context) {
    val TAG = javaClass.simpleName

    var account = ""
    var password = ""
    val livePgDialogNeedShow by lazy { MutableLiveData<Boolean>() }
    var userIndex = 0
    /**
     * Status Code (Enum)
     * */
    enum class Status {
        Initial,
        AccountOrPasswordIsEmpty,
        AccountCorrectButPasswordError,
        AccountDontExist,
        AccountCorrectAndPasswordCorrect
    }

    val liveStatus by lazy { MutableLiveData<Status>() }

    private val loginDuration by lazy {
        context.resources.getInteger(R.integer.login_duration).toLong()
    }

    fun login() {

        loge(TAG, "now userArray before login is ===>$userArray")
        if (account == "" || password == "") {
            liveStatus.postValue(Status.AccountOrPasswordIsEmpty)
            return
        }

       viewModelScope.launch(Dispatchers.IO) {
            initLiveDataValue()
            val startTime = Date().time
            var isFail = false // if Account in userArray,But Password is incorrect, this boolean will be true
            var isMember = false // if Account in userArray, and Password is correct, this boolean will be true
            for (index in userArray.indices) {
                if (userArray[index].account == account) {
                    if (userArray[index].password == password) {
                        isMember = true
                        userIndex = index
                    } else
                        isFail = true
                    break
                }
            }

            delay(startTime.getWaitInterval(loginDuration))

            livePgDialogNeedShow.postValue(false)
            when {
                isFail -> {
                    liveStatus.postValue(Status.AccountCorrectButPasswordError)
                }
                isMember -> { // For Login Activity To Member Activity
                    liveStatus.postValue(Status.AccountCorrectAndPasswordCorrect)
                }
                else -> { //For Login Activity post to Api And To Member Activity
                    liveStatus.postValue(Status.AccountDontExist)
                }
            }
        }
    }

    private fun initLiveDataValue() {
        liveStatus.postValue(Status.Initial)
        livePgDialogNeedShow.postValue(true)
    }

    /**Send Api to new this User Data*/
    @Throws(Exception::class)
    fun upLoadUserData(user: UserModelData.UserModelItem): UserModelData.UserModelItem? {
        val json = JsonObject().apply {
            addProperty("account", user.account)
            addProperty("password", user.password)
        }

        val cell = ApiConnect.getService(context).uploadData(json)
        val response = cell.execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            loge(TAG, "API ERROR! this is error message：${response.errorBody()?.string()}")
            null
        }
    }



}
class LoginFactory(private val application: Application, private val userArray: ArrayList<UserModelData.UserModelItem>) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  LoginViewModel(application, userArray) as T
    }
}
