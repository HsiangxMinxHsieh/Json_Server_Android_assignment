package com.timmymike.json_server_android_assignment.api.model

import com.google.gson.annotations.SerializedName


class UserModelData : ArrayList<UserModelData.UserModelItem>() {
    data class UserModelItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("password")
        var password: String = "",
        @SerializedName("user")
        var user: String = ""
    )
}