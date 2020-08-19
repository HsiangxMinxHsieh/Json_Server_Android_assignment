package com.timmymike.json_server_android_assignment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.timmymike.json_server_android_assignment.databinding.ActivitySplashSettingBinding
import com.timmymike.json_server_android_assignment.mvvm.SplashViewModel
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.dialog.showMessageDialogOnlyOKButton
import com.timmymike.json_server_android_assignment.tools.loge
import com.timmymike.json_server_android_assignment.tools.setTextSize

class SplashSettingActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName
    private val context: Context = this
    private val activity = this
    private lateinit var splashBinding: ActivitySplashSettingBinding
    private lateinit var viewModel: SplashViewModel

    val pgDialg: ProgressDialog by lazy {
        ProgressDialog(context).apply {
            needClose = true
            binding.ivClose.setOnClickListener {
                dialog.dismiss()
                viewModel.liveLoadingInterrupt.postValue(true)
                viewModel.job?.cancel()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash_setting)
        splashBinding = DataBindingUtil.setContentView(activity, R.layout.activity_splash_setting)

        initView()

        initMvvm()

        initObserver()

    }

    private fun initObserver() {

        viewModel.livePgDialogNeedShow.observe(activity, Observer {
            if (it)
                pgDialg.show()
            else
                pgDialg.dismiss()
        })

        viewModel.liveGetData.observe(activity, Observer {

            if (it == null || it.isEmpty()) {
                loge(TAG, "getUserData do not get the data.")
                viewModel.liveLoadingInterrupt.postValue(true)
                showMessageDialogOnlyOKButton(context, context.getString(R.string.error_dialog_title), context.getString(R.string.splash_no_data_get_error_message))
            } else {
                //To Login
                val intent = Intent(context, LoginActivity::class.java)
                intent.putParcelableArrayListExtra(LoginActivity.KEY_USER_DATA, it)
                startActivity(intent)
                viewModel.livePgDialogNeedShow.postValue(false)
                activity.finish()
            }
        })
    }

    private fun initView() {
        splashBinding.edtUrl.setTextSize(20)
    }

    private fun initMvvm() {
        viewModel = ViewModelProvider(activity, ViewModelProvider.AndroidViewModelFactory(application)).get(SplashViewModel::class.java)

        splashBinding.viewModel = viewModel
        splashBinding.lifecycleOwner = activity
    }

    override fun onDestroy() {
        super.onDestroy()
        if (pgDialg.isShowing())
            pgDialg.dismiss()
    }

}
