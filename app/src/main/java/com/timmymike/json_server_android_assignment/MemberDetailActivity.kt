package com.timmymike.json_server_android_assignment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.databinding.ActivityMemberDetailBinding
import com.timmymike.json_server_android_assignment.mvvm.MemberDetailViewModel
import com.timmymike.json_server_android_assignment.mvvm.ViewMemberFactory
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.setTextSize
import java.io.Serializable

class MemberDetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_USER_DATA = "KEY_USER_DATA"
        const val KEY_LOGIN_METHOD = "KEY_LOGIN_METHOD"

        enum class LoginMethod : Serializable {
            SignUp,
            Login
        }
    }

    private val context: Context = this
    private val activity = this
    private var userData: UserModelData.UserModelItem? = null
    private var loginStatus = LoginMethod.Login
    private lateinit var viewModel: MemberDetailViewModel
    private lateinit var memberBinding: ActivityMemberDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        memberBinding = DataBindingUtil.setContentView(activity, R.layout.activity_member_detail)

        initData()

        initView()

        initMvvm()

        initObserver()

    }

    private fun initData() {
        try {
            loginStatus = intent.getSerializableExtra(KEY_LOGIN_METHOD) as LoginMethod
            userData = intent.getParcelableExtra(KEY_USER_DATA) as UserModelData.UserModelItem
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        val textSize = 20
        memberBinding.tvInformationTitle.setTextSize(textSize)
        memberBinding.tvLoginMethod.setTextSize(textSize)
        memberBinding.tvId.setTextSize(textSize)
        memberBinding.tvAccount.setTextSize(textSize)
        memberBinding.tvPassword.setTextSize(textSize)
    }

    private fun initMvvm() {
        viewModel = ViewModelProvider(activity, ViewMemberFactory(application, loginStatus, userData ?: UserModelData.UserModelItem())).get(MemberDetailViewModel::class.java)

        memberBinding.viewModel = viewModel
        memberBinding.lifecycleOwner = activity
    }

    private fun initObserver() {
        viewModel.liveNeedFinish.observe(activity, Observer {
            if (it)
                activity.finish()
        })
    }
}
