package tech.devbashar.interactivetaskmanager.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.formatDate(): Date {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.parse(this)!!
}

fun Date.formatDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(this)
}
