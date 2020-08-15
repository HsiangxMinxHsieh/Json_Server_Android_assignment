package com.timmymike.json_server_android_assignment.mvvm

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.MemberDetailActivity
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.dialog.TextDialog
import com.timmymike.json_server_android_assignment.tools.dialog.showMessageDialogOnlyOKButton
import com.timmymike.json_server_android_assignment.tools.getWaitInterval
import com.timmymike.json_server_android_assignment.tools.loge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**======== View Model ========*/

class LoginViewModel(private val context: Context, private val userArray: ArrayList<UserModelData.UserModelItem>) : ViewModel() {
    val TAG = javaClass.simpleName

    var account = ""
    var password = ""

    private val loginDuration by lazy {
        context.resources.getInteger(R.integer.login_duration).toLong()
    }

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

        GlobalScope.launch {

            GlobalScope.launch(Dispatchers.Main) {
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

            GlobalScope.launch(Dispatchers.Main) {
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
            val userData = if (userIndexInArray != -1) userArray[userIndexInArray] else UserModelData.UserModelItem()
            val intent = Intent(context,MemberDetailActivity::class.java)
            if (isMember) { // To Member Activity
                intent.putExtra(MemberDetailActivity.KEY_LOGIN_METHOD, MemberDetailActivity.Companion.LoginMethod.Login)

            } else { // post to Api And To Member Activity
                intent.putExtra(MemberDetailActivity.KEY_LOGIN_METHOD, MemberDetailActivity.Companion.LoginMethod.SignUp)

            }
            intent.putExtra(MemberDetailActivity.KEY_USER_DATA,userData)
            (context as? Activity)?.startActivity(intent)



        }
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

