package com.timmymike.json_server_android_assignment.tools.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.databinding.DialogProgressBinding
import com.timmymike.json_server_android_assignment.tools.setTextSize

class ProgressDialog(val context: Context) {
    var title = context.getString(R.string.dialog_progress_default_title)
    var needClose = false
    val dialog by lazy { MaterialDialog(context) }
    val binding by lazy { DataBindingUtil.inflate<DialogProgressBinding>(LayoutInflater.from(context), R.layout.dialog_progress, null, false) }

    fun isShowing(): Boolean = dialog.isShowing
    fun dismiss() = dialog.dismiss()

    fun show(){
        if (dialog.isShowing){
            return
        }
        //initial content
        binding.apply {
            root.setBackgroundColor(context.getColor(R.color.white))
            tvTitle.text = title
            tvTitle.setTextSize(16)

            if(needClose)
                ivClose.visibility = View.VISIBLE
            else
                ivClose.visibility = View.GONE

            if (title == ""){
                tvTitle.visibility = View.GONE
            }
        }
        //to show
        dialog.apply {
            setContentView(binding.root)
            setCancelable(false)
            window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            show()
        }
    }
}