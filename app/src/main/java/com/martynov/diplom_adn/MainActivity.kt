package com.martynov.diplom_adn

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.authorization)
        if(isAuthenticated()){
            navigateToFeed()
            return
        }


        btn_login.setOnClickListener {
            when {
                !isValidUsername(login_text.text.toString()) -> {
                    textInputLogin.error = getString(R.string.bad_login)
                }
                !isValidPassword(password_text.text.toString()) -> {
                    textInputPassword.error = getString(R.string.bad_password)
                }
                else -> {
                    lifecycleScope.launch {
                        dialog = ProgressDialog(this@MainActivity).apply {
                            setMessage(getString(R.string.please_wait))
                            setTitle(getString(R.string.loading_data))
                            show()
                            setCancelable(false)
                        }
                        val login = login_text.text?.toString().orEmpty()
                        val password = password_text.text?.toString().orEmpty()
                        try {
                            val token = App.repository.authenticate(login, password)
                            dialog?.dismiss()
                            if (token.isSuccessful) {
                                setUserAuth(requireNotNull(token.body()?.token))
                                requestToken()
                                navigateToFeed()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(R.string.authorisation_Error),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.falien_connect),
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog?.dismiss()
                        }
                    }
                }
            }
        }
        btn_registration.setOnClickListener {
            navigateToRegistration()
        }
    }

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .apply()

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@MainActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }

            if (isUserResolvableError(code)) {
                getErrorDialog(this@MainActivity, code, 9000).show()
                return
            }

            root.longSnackbar(getString(R.string.google_play_unavailable))
            return
        }

    }

    private fun navigateToFeed() {
        val intent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegistration() {
        val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun isAuthenticated(): Boolean =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

}
