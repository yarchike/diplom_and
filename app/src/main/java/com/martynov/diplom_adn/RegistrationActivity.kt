package com.martynov.diplom_adn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        title = getString(R.string.registration)
    }
}