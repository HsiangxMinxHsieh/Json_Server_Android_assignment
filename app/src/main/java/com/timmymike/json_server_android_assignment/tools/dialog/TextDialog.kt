package com.timmymike.json_server_android_assignment.tools.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.timmymike.json_server_android_assignment.R
import com.timmymike.json_server_android_assignment.databinding.DialogTextBinding
import com.timmymike.json_server_android_assignment.tools.setTextSize

fun showMessageDialogOnlyOKButton(context: Context, message: String, lambda: () -> Unit = {}): TextDialog {
    return TextDialog(context).apply {
        title = message
        dialogBinding.btnLift.visibility = View.GONE
        dialogBinding.btnRight.text = context.getString(R.string.ok)
        dialogBinding.btnRight.setOnClickListener {
            lambda()
            dialog.dismiss()
        }
        show()
    }
}

fun showMessageDialogOnlyOKButton(context: Context, title: String, message: String, lambda: () -> Unit = {}): TextDialog {
    return TextDialog(context).apply {
        this.title = title
        this.message = message
        dialogBinding.btnLift.visibility = View.GONE
        dialogBinding.btnRight.text = context.getString(R.string.ok)
        dialogBinding.btnRight.setOnClickListener {
            lambda()
            dialog.dismiss()
        }
        show()
    }
}


class TextDialog(val context: Context) {
    var title = ""
    var message = ""

    val dialog by lazy { MaterialDialog(context) }
    val dialogBinding by lazy { DataBindingUtil.inflate<DialogTextBinding>(LayoutInflater.from(context), R.layout.dialog_text, null, false) }
    fun show() {
        if (dialog.isShowing) {
            return
        }
        //
        dialogBinding.apply {
            val backgroundCorner = 25
            root.setBackgroundColor(context.getColor(R.color.white))
            tvTitle.text = title
            tvTitle.setTextSize(20)
            if (title == "") {
                tvTitle.visibility = View.GONE
            }
            tvMessage.text = message
            tvMessage.setTextSize(16)
            if (message == "") {
                tvMessage.visibility = View.GONE
            }
        }
        //
        dialog.apply {

            setContentView(dialogBinding.root)
            setCancelable(false)
            window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            show()
        }
    }
}