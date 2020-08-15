package com.timmymike.json_server_android_assignment.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UserModelData : ArrayList<UserModelData.UserModelItem>() {
    data class UserModelItem(
        @SerializedName("account")
        var account: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("password")
        var password: String = ""
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(account)
            parcel.writeInt(id)
            parcel.writeString(password)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<UserModelItem> {
            override fun createFromParcel(parcel: Parcel): UserModelItem {
                return UserModelItem(parcel)
            }

            override fun newArray(size: Int): Array<UserModelItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}