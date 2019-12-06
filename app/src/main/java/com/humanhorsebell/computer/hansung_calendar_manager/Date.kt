package com.humanhorsebell.computer.hansung_calendar_manager

import android.os.Parcel
import android.os.Parcelable

data class Date(val day: String, val time: String) : Comparable<Date>, Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun compareTo(other: Date): Int {
        if(day > other.day) return 1
        if(day < other.day) return -1
        if(time > other.time) return 1;
        if(time < other.time) return -1;
        return 0;
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(day)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Date> {
        override fun createFromParcel(parcel: Parcel): Date {
            return Date(parcel)
        }

        override fun newArray(size: Int): Array<Date?> {
            return arrayOfNulls(size)
        }
    }
}