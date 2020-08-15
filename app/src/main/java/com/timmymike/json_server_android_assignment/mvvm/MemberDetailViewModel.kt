package com.timmymike.json_server_android_assignment.mvvm

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.MemberDetailActivity
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.tools.dialog.TextDialog
import com.timmymike.json_server_android_assignment.tools.formatByResourceID
import com.timmymike.json_server_android_assignment.tools.loge

/**======== View Model ========*/

class MemberDetailViewModel(private val context: Context, private val loginMethod: MemberDetailActivity.Companion.LoginMethod, private val userData: UserModelData.UserModelItem) : ViewModel() {
    val TAG = javaClass.simpleName

    var id = ""
    var account = ""
    var password = ""
    var loginMethodShowString = ""

    init {
        id = userData.id.toString().formatByResourceID(context,R.string.member_id)
        account = userData.account.formatByResourceID(context,R.string.member_account)
        password = userData.password.getCircleByLength().formatByResourceID(context,R.string.member_password)
        loge(TAG,"ID===>$id")
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
        (context as? Activity)?.onBackPressed()
    }
}

class ViewModelMemberFactory(private val context: Context, private val loginMethod: MemberDetailActivity.Companion.LoginMethod, private val userData: UserModelData.UserModelItem) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemberDetailViewModel::class.java)) {
            return MemberDetailViewModel(context, loginMethod, userData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

