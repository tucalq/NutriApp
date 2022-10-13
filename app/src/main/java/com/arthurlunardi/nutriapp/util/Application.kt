package com.arthurlunardi.nutriapp.util

import android.app.Application
import android.text.format.DateFormat
import java.util.*


class Application : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {

        fun formatTimeStamp(timestamp: Long) : String {
            val cal = Calendar.getInstance(Locale.US)
            cal.timeInMillis = timestamp
            // format dd/MM/yy
            return DateFormat.format("dd/MM/yyyy", cal).toString()
        }

    }

}