package com.timmymike.json_server_android_assignment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.timmymike.json_server_android_assignment.api.model.UserModelData
import com.timmymike.json_server_android_assignment.databinding.ActivityLoginBinding
import com.timmymike.json_server_android_assignment.mvvm.LoginFactory
import com.timmymike.json_server_android_assignment.mvvm.LoginViewModel
import com.timmymike.json_server_android_assignment.tools.dialog.ProgressDialog
import com.timmymike.json_server_android_assignment.tools.dialog.TextDialog
import com.timmymike.json_server_android_assignment.tools.dialog.showMessageDialogOnlyOKButton
import com.timmymike.json_server_android_assignment.tools.setTextSize
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    companion object {
        const val KEY_USER_DATA = "KEY_USER_DATA"
    }

    private val context: Context = this
    private val activity = this
    private var userDataArray = ArrayList<UserModelData.UserModelItem>()
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginBinding: ActivityLoginBinding
    private val liveShowDialog by lazy { MutableLiveData<String>() }

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
        viewModel = ViewModelProviders.of(this, LoginFactory(activity.application, userDataArray)).get<LoginViewModel>(LoginViewModel::class.java)
        loginBinding.viewModel = viewModel
        loginBinding.lifecycleOwner = activity
    }

    private var textDialog: TextDialog? = null

    private fun initObserver() {
        viewModel.livePgDialogNeedShow.observe(activity, Observer {
            if (it)
                pgDialg.show()
            else
                pgDialg.dismiss()
        })
        viewModel.liveStatus.observe(activity, Observer {
            GlobalScope.launch {
                var userData: UserModelData.UserModelItem? = null
                val intent = Intent(context, MemberDetailActivity::class.java)

                when (it) {
                    LoginViewModel.Status.Initial -> return@launch
                    LoginViewModel.Status.AccountOrPasswordIsEmpty -> {
                        liveShowDialog.postValue(context.getString(R.string.login_account_or_password_empty))
                        return@launch
                    }
                    LoginViewModel.Status.AccountCorrectButPasswordError -> {// Fail
                        liveShowDialog.postValue(context.getString(R.string.login_account_right_password_error))
                        return@launch
                    }
                    LoginViewModel.Status.AccountCorrectAndPasswordCorrect -> { // only get Data
                        userData = viewModel.userArray[viewModel.userIndex]
                        intent.putExtra(MemberDetailActivity.KEY_LOGIN_METHOD, MemberDetailActivity.Companion.LoginMethod.Login)
                    }
                    LoginViewModel.Status.AccountDontExist -> { // post to Api
                        intent.putExtra(MemberDetailActivity.KEY_LOGIN_METHOD, MemberDetailActivity.Companion.LoginMethod.SignUp)

                        userData = viewModel.upLoadUserData(
                            UserModelData.UserModelItem().apply {
                                account = viewModel.account
                                password = viewModel.password
                            }) ?: UserModelData.UserModelItem()
                        viewModel.userArray.add(userData)
                    }
                }
                //to Member Page
                intent.putExtra(MemberDetailActivity.KEY_USER_DATA, userData)
                activity.startActivity(intent)

            }
        })
        liveShowDialog.observe(activity, Observer {
            textDialog = showMessageDialogOnlyOKButton(context, context.getString(R.string.error_dialog_title), it) {
                textDialog = null
                viewModel.liveStatus.postValue(LoginViewModel.Status.Initial)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        if (pgDialg.isShowing())
            pgDialg.dismiss()
        if (textDialog?.isShowing() == true)
            textDialog?.dismiss()
    }

}
