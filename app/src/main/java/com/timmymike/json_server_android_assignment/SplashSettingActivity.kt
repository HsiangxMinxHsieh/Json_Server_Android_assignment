package com.timmymike.json_server_android_assignment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.mvvm.SplashViewModel
import com.timmymike.json_server_android_assignment.mvvm.ViewModelSplashFactory
import com.timmymike.json_server_android_assignment.databinding.ActivitySplashSettingBinding
import com.timmymike.json_server_android_assignment.tools.setTextSize

class SplashSettingActivity : AppCompatActivity() {
    private val context: Context = this
    private val activity = this
    private lateinit var splashBinding: ActivitySplashSettingBinding
    private lateinit var viewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash_setting)
        splashBinding = DataBindingUtil.setContentView(activity, R.layout.activity_splash_setting)

        initView()

        initMvvm()

    }

    private fun initView() {
        splashBinding.edtUrl.setTextSize(20)
    }

    private fun initMvvm() {
        viewModel = ViewModelProvider(activity, ViewModelSplashFactory(context)).get(SplashViewModel::class.java)

        splashBinding.viewModel = viewModel
        splashBinding.lifecycleOwner = activity
    }


}
