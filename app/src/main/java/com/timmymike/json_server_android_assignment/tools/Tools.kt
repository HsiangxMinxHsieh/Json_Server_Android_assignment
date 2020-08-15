package com.timmymike.json_server_android_assignment.tools

import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.timmymike.json_server_android_assignment.BuildConfig

fun logi(tag: String, log: Any) {
    if (BuildConfig.DEBUG_MODE) Log.i(tag, log.toString())
}

fun loge(tag: String, log: Any) {
    if (BuildConfig.DEBUG_MODE) Log.e(tag, log.toString())
}

fun View.setTextSize(sp: Int) {
    val displayMetrics = this.context.resources.displayMetrics
    val realSpSize =
        ((sp * displayMetrics.widthPixels).toFloat() / displayMetrics.density / 360f).toInt()
    when (this) {
        is TextView -> this.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
        is Button -> this.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
        is EditText -> this.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
        else -> (this as TextView).setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())

    }
}