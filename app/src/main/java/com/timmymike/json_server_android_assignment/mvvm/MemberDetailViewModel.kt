package com.timmymike.json_server_android_assignment.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.MemberDetailActivity
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.formatByResourceID
import com.timmymike.json_server_android_assignment.tools.loge

/**======== View Model ========*/

class MemberDetailViewModel(private val context: Application, private val loginMethod: MemberDetailActivity.Companion.LoginMethod, private val userData: UserModelData.UserModelItem) : AndroidViewModel(context) {
    val TAG = javaClass.simpleName

    var id = ""
    var account = ""
    var password = ""
    var loginMethodShowString = ""
    val liveNeedFinish by lazy { MutableLiveData<Boolean>() }

    init {
        id = userData.id.toString().formatByResourceID(context, R.string.member_id)
        account = userData.account.formatByResourceID(context, R.string.member_account)
        password = userData.password.getCircleByLength().formatByResourceID(context, R.string.member_password)
        loge(TAG, "ID===>$id")
        showData()
    }

    /**only Show black circle in screen*/
    private fun String.getCircleByLength(): String {
        var result = ""
        for (i in this.indices) {
            result += "●"
        }
        return result
    }

    fun showData() {
        loge(TAG, "now userData is ===>$userData")
        loginMethodShowString = if (loginMethod == MemberDetailActivity.Companion.LoginMethod.Login)
            context.getString(R.string.member_login_method_login)
        else
            context.getString(R.string.member_login_method_sign_up)

    }

    fun back() {
        liveNeedFinish.postValue(true)
    }
}

class ViewMemberFactory(private val application: Application, private val loginMethod: MemberDetailActivity.Companion.LoginMethod, private val userData: UserModelData.UserModelItem) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemberDetailViewModel(application, loginMethod, userData) as T
    }
}