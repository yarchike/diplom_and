package com.martynov.diplom_adn

import android.view.View
import com.google.android.material.snackbar.Snackbar

inline fun View.longSnackbar(message: CharSequence, length: Int = Snackbar.LENGTH_LONG) = Snackbar
    .make(this, message, length)
    .apply { show() }