
package com.example.wellcheck.Domain

import android.os.Parcel
import android.os.Parcelable

data class Doctors(
    var id: String = "",
    var name: String = "",
    var picture: String = "",
    var biography: String = "",
    var address: String = "",
    var special: String = "",
    var location: String = "",
    var mobile: String = "",
    var patiens: String = "",
    var site: String = "",
    var rating: Double = 0.0,
    var expriense: Double = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(picture)
        parcel.writeString(biography)
        parcel.writeString(address)
        parcel.writeString(special)
        parcel.writeString(location)
        parcel.writeString(mobile)
        parcel.writeString(patiens)
        parcel.writeString(site)
        parcel.writeDouble(rating)
        parcel.writeDouble(expriense)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Doctors> {
        override fun createFromParcel(parcel: Parcel): Doctors = Doctors(parcel)
        override fun newArray(size: Int): Array<Doctors?> = arrayOfNulls(size)
    }
}
