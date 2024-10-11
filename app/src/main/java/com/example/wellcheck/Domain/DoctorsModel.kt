package com.example.wellcheck.Domain

import android.os.Parcel
import android.os.Parcelable

data class DoctorsModel(
    val Id:Int=0,
    val Name:String="",
    val Picture:String="",
    val Biography:String="",
    val Address:String="",
    val Special:String="",
    val Location:String="",
    val Mobile:String="",
    val Patiens:String="",
    val Site:String="",
    val Rating:Double=0.0,
    val Expriense:Double=0.0
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Id)
        parcel.writeString(Name)
        parcel.writeString(Picture)
        parcel.writeString(Biography)
        parcel.writeString(Address)
        parcel.writeString(Special)
        parcel.writeString(Location)
        parcel.writeString(Mobile)
        parcel.writeString(Patiens)
        parcel.writeString(Site)
        parcel.writeDouble(Rating)
        parcel.writeDouble(Expriense)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DoctorsModel> {
        override fun createFromParcel(parcel: Parcel): DoctorsModel {
            return DoctorsModel(parcel)
        }

        override fun newArray(size: Int): Array<DoctorsModel?> {
            return arrayOfNulls(size)
        }
    }

}
