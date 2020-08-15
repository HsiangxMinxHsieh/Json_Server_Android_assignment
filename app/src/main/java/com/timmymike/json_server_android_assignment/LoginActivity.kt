package com.timmymike.json_server_android_assignment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.databinding.ActivityLoginBinding
import com.timmymike.json_server_android_assignment.mvvm.LoginViewModel
import com.timmymike.json_server_android_assignment.mvvm.SplashViewModel
import com.timmymike.json_server_android_assignment.mvvm.ViewModelLoginFactory
import com.timmymike.json_server_android_assignment.mvvm.ViewModelSplashFactory
import com.timmymike.json_server_android_assignment.tools.loge

class LoginActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName
    companion object{
        const val KEY_USER_DATA= "KEY_USER_DATA"

    }
    private val context: Context = this
    private val activity = this
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(activity, R.layout.activity_login)

        initData()

        initMvvm()
//        setContentView(R.layout.activity_login)
    }
    private fun initData() {
        loge(TAG,"${intent.getParcelableArrayListExtra<UserModelData.UserModelItem>(KEY_USER_DATA)}")
        val bundle = intent.extras

        var s = ""
        if (bundle != null){
            for (key in bundle.keySet()) {
                try {
                    val value = bundle.get(key)
                    s += "$key:$value\n"
                    loge(TAG, "$key $value")
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
            loge(TAG, "" + s)
        }

    }
    private fun initMvvm() {
        viewModel = ViewModelProvider(activity, ViewModelLoginFactory(context)).get(LoginViewModel::class.java)

        loginBinding.viewModel = viewModel
        loginBinding.lifecycleOwner = activity
    }
}
