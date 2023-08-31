package com.example.myapplication

import android.os.Parcel
import android.os.Parcelable

class taskModel() : Parcelable {
    lateinit var taskName:String

    lateinit var hour:String
    lateinit var minute:String
    var done:Boolean = false
    val mPriority:Int
        get() = hour.toInt()*100 + minute.toInt()

    constructor(parcel: Parcel) : this() {
        taskName = parcel.readString().toString()
        hour = parcel.readString().toString()
        minute = parcel.readString().toString()
        done = parcel.readByte() != 0.toByte()
    }

    constructor(taskName: String, hour: String, minute:String, done:Boolean) : this() {
        this.taskName = taskName
        this.hour = hour
        this.minute = minute
        this.done = done
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskName)
        parcel.writeString(hour)
        parcel.writeString(minute)
        parcel.writeByte(if (done) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<taskModel> {
        override fun createFromParcel(parcel: Parcel): taskModel {
            return taskModel(parcel)
        }

        override fun newArray(size: Int): Array<taskModel?> {
            return arrayOfNulls(size)
        }
    }

}