package com.martynov.diplom_adn

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun isValidPassword(password: String) =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,15})\$").matcher(password).matches()
fun isValidUsername(username: String) =
    Pattern.compile("(.{1,10})\$").matcher(username).matches()
@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Long): String{
    val cal: Calendar = Calendar.getInstance()
    cal.timeInMillis = date
    val format = SimpleDateFormat("dd-MM-yyyy hh:mm");
    return format.format(cal.time)
}