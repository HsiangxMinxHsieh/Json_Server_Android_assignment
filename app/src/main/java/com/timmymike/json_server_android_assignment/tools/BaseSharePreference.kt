package com.timmymike.json_server_android_assignment.tools

import android.content.Context
import com.timmymike.json_server_android_assignment.api.ApiConnect

/**
 * Description:
 * @author Robert Chou didi31139@gmail.com
 * @date 2015/5/27 下午1:45:58
 * @version
 */
object BaseSharePreference {
    private val TABLENAME = "shear"

    /** URL Link*/
    private val KEY_URL_LINK = "KEY_URL_LINK"

    fun getString(
        context: Context,
        key: String,
        defValues: String,
        tableName: String = TABLENAME
    ): String {
        val sharedPreferences = context.getSharedPreferences(tableName, 0)
        return sharedPreferences.getString(key, defValues) ?: defValues
    }

    fun putString(context: Context, key: String, value: String, tableName: String = TABLENAME) {
        val sharedPreferences = context.getSharedPreferences(tableName, 0)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    /** set URL link */
    fun setURLLink(context: Context, url: String) {
        putString(context, KEY_URL_LINK, url)
    }

    /** get URL link */
    fun getURLLink(context: Context): String {
        return getString(context, KEY_URL_LINK, ApiConnect.def_url)
    }
}