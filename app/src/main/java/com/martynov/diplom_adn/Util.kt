package com.martynov.diplom_adn

import java.util.regex.Pattern

fun isValidPassword(password: String) =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,15})\$").matcher(password).matches()
fun isValidUsername(username: String) =
    Pattern.compile("(.{1,10})\$").matcher(username).matches()