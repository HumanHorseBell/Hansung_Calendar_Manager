package com.humanhorsebell.computer.hansung_calendar_manager

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Schedule(val grpName : String, val startDate : Date, val endDate : Date, val picture : String?, val name : String) : Comparable<Schedule>, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(Date::class.java.classLoader),
            parcel.readParcelable(Date::class.java.classLoader),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun compareTo(other: Schedule): Int {
        if(startDate > other.startDate) return 1
        if(startDate < other.startDate) return -1
        if(name > other.name) return 1
        if(name < other.name) return -1
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(grpName)
        parcel.writeParcelable(startDate, flags)
        parcel.writeParcelable(endDate, flags)
        parcel.writeString(picture)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }
}
