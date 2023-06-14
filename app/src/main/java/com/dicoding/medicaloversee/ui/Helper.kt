package com.dicoding.medicaloversee.ui

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun TextView.withDateFormat(timestamp: String?) {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("dd MMMM yyyy, EEEE HH:mm", Locale.US)

    val date = timestamp?.let { inputFormat.parse(it) } ?: Date()
    val formattedDate = outputFormat.format(date)

    this.text = formattedDate
}
