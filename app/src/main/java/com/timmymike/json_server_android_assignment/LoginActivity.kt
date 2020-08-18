package com.timmymike.json_server_android_assignment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.databinding.ActivityLoginBinding
import com.timmymike.json_server_android_assignment.mvvm.LoginViewModel
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.setTextSize

class LoginActivity : AppCompatActivity() {

    companion object {
        const val KEY_USER_DATA = "KEY_USER_DATA"
    }

    private val context: Context = this
    private val activity = this
    private var userDataArray = ArrayList<UserModelData.UserModelItem>()
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginBinding: ActivityLoginBinding

    val pgDialg: ProgressDialog by lazy { ProgressDialog(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(activity, R.layout.activity_login)

        initData()

        initView()

        initMvvm()

        initObserver()
    }


    private fun initData() {
        try {
            userDataArray = intent.getParcelableArrayListExtra<UserModelData.UserModelItem>(KEY_USER_DATA) as ArrayList<UserModelData.UserModelItem>

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        loginBinding.edtAccount.setTextSize(20)
        loginBinding.edtPassword.setTextSize(20)
    }

    private fun initMvvm() {
        viewModel = ViewModelProvider(activity, ViewModelProvider.AndroidViewModelFactory(application)).get(LoginViewModel::class.java)

        loginBinding.viewModel = viewModel
        loginBinding.lifecycleOwner = activity
    }

    private fun initObserver() {
        viewModel.livePgDialogNeedShow.observe(activity, Observer {
            if (it )
                pgDialg.show()
            else
                pgDialg.dismiss()
        })


    }
}
