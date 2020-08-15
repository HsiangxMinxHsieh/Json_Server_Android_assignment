package com.timmymike.json_server_android_assignment.tools

import android.util.Log
import com.timmymike.json_server_android_assignment.BuildConfig

fun logi(tag: String, log: Any) {
    if (BuildConfig.DEBUG_MODE) Log.i(tag, log.toString())
}

fun loge(tag: String, log: Any) {
    if (BuildConfig.DEBUG_MODE) Log.e(tag, log.toString())
}