package com.timmymike.json_server_android_assignment.mvvm

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.timmymike.json_server_android_assignment.MemberDetailActivity
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.ApiConnect
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.dialog.TextDialog
import com.timmymike.json_server_android_assignment.tools.dialog.showMessageDialogOnlyOKButton
import com.timmymike.json_server_android_assignment.tools.getWaitInterval
import com.timmymike.json_server_android_assignment.tools.loge
import kotlinx.coroutines.*
import java.util.*

/**======== View Model ========*/

class LoginViewModel(private val context: Context, private val userArray: ArrayList<UserModelData.UserModelItem>) : ViewModel() {
    val TAG = javaClass.simpleName

    var account = ""
    var password = ""

    private val loginDuration by lazy {
        context.resources.getInteger(R.integer.login_duration).toLong()
    }
    private var job: Job? = null
    private var textDialog: TextDialog? = null
    fun login() {
        loge(TAG, "now userArray before login is ===>$userArray")
        if (account == "" || password == "") {
            textDialog = showMessageDialogOnlyOKButton(context, context.getString(R.string.error_dialog_title), context.getString(R.string.login_account_or_password_empty)) {
                textDialog = null
            }
            return
        }

        val pgDialg = ProgressDialog(context)

        job = viewModelScope.launch(Dispatchers.IO) {

            viewModelScope.launch(Dispatchers.Main) {
                if (!pgDialg.isShowing())
                    pgDialg.show()
            }
            val startTime = Date().time

            var isFail = false // if Account in userArray,But Password is incorrect, this boolean will be true
            var isMember = false // if Account in userArray, and Password is correct, this boolean will be true
            var userIndexInArray = -1
            for (index in userArray.indices) {
                if (userArray[index].account == account) {
                    if (userArray[index].password == password) {
                        isMember = true
                        userIndexInArray = index
                    } else
                        isFail = true
                    break
                }
            }
            delay(startTime.getWaitInterval(loginDuration))

            viewModelScope.launch(Dispatchers.Main) {
                if (pgDialg.isShowing()) {
                    pgDialg.dismiss()
                }
                if (isFail) {//showTextDialog
                    textDialog = showMessageDialogOnlyOKButton(context, context.getString(R.string.error_dialog_title), context.getString(R.string.login_account_right_password_error)) {
                        textDialog = null
                    }
                    return@launch
                }
            }
            if (!isFail) {
                var userData = if (userIndexInArray != -1) userArray[userIndexInArray] else UserModelData.UserModelItem()
                val intent = Intent(context, MemberDetailActivity::class.java)
                if (isMember) { // To Member Activity
                    intent.putExtra(MemberDetailActivity.KEY_LOGIN_METHOD, MemberDetailActivity.Companion.LoginMethod.Login)

                } else { // post to Api And To Member Activity
                    intent.putExtra(MemberDetailActivity.KEY_LOGIN_METHOD, MemberDetailActivity.Companion.LoginMethod.SignUp)
                    userData = upLoadUserData(
                        UserModelData.UserModelItem().apply {
                        account = this@LoginViewModel.account
                        password = this@LoginViewModel.password
                    }) ?: UserModelData.UserModelItem()
                    userArray.add(userData)
                }
                intent.putExtra(MemberDetailActivity.KEY_USER_DATA, userData)
                (context as? Activity)?.startActivity(intent)
            }


        }
    }

    /**Send Api to new this User Data*/
    @Throws(Exception::class)
    private fun upLoadUserData(user: UserModelData.UserModelItem): UserModelData.UserModelItem? {
        val json = JsonObject().apply {
            addProperty("account", user.account)
            addProperty("password", user.password)
        }

        val cell = ApiConnect.getService(context).uploadData(json)
        val response = cell.execute()
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
        loge(TAG,"ViewModelCleared.")
    }


}

class ViewModelLoginFactory(private val context: Context, private val userArray: ArrayList<UserModelData.UserModelItem>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context, userArray) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

