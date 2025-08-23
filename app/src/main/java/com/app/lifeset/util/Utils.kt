package com.app.lifeset.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


object Utils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateFormat(date: String): String {
        val date = LocalDate.parse(date)
        println(date)
        val formatter = DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.ENGLISH)
        val formatted = date.format(formatter)
        return formatted
    }
}