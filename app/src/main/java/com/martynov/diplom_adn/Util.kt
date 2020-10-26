package com.martynov.diplom_adn

import android.annotation.SuppressLint
import com.martynov.diplom_adn.model.IdeaModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun isValidPassword(password: String) =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,15})\$").matcher(password).matches()
fun isValidUsername(username: String) =
    Pattern.compile("(.{1,10})\$").matcher(username).matches()
fun isValidUrl(url:String) =
    Pattern.compile("^(https?:\\/\\/)?([\\w-]{1,32}\\.[\\w-]{1,32})[^\\s@]*\$").matcher(url).matches()
@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Long): String{
    val cal: Calendar = Calendar.getInstance()
    cal.timeInMillis = date
    val format = SimpleDateFormat("dd.MMMyyyy HH:mm")
    return format.format(cal.time)
}