package com.martynov.diplom_adn

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.AttachmentType
import com.martynov.diplom_adn.data.PasswordChangeRequestDto
import kotlinx.android.synthetic.main.activity_user_settings.*
import kotlinx.android.synthetic.main.tollbar.*
import kotlinx.coroutines.launch
import splitties.toast.toast

class UserSettingsActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var attachmentModel: AttachmentModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)
        title = getString(R.string.settings)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        buttonChangePhoto.setOnClickListener {
            lifecycleScope.launch {
                if (attachmentModel != null) {
                    try {
                        val ok = App.repository.changeImg(attachmentModel = attachmentModel!!)
                        if (ok.isSuccessful) {
                            toast("Фото успешно измененено")
                            attachPhotoDoneImgSetting.visibility = View.GONE
                            attachPhotoImgSetting.setImageResource(R.drawable.ic_image)
                            attachmentModel = null
                        }
                    } catch (e: Exception) {
                        toast(getString(R.string.falien_connect))
                    }
                } else {
                    toast(getString(R.string.error_change_image))
                }
            }

        }

        button_exit_account.setOnClickListener {
            getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
                .edit()
                .remove(AUTHENTICATED_SHARED_KEY)
                .apply()
            val intent = Intent(this@UserSettingsActivity, MainActivity::class.java)
            startActivity(intent)
        }
        buttonChangePassword.setOnClickListener {
            lifecycleScope.launch {
                val password = passwordText.text?.toString().orEmpty()
                val twoPassword = passwordTwoText.text?.toString().orEmpty()
                val oldPassword = oldPasswordText.text?.toString().orEmpty()
                when {
                    !isValidPassword(oldPassword) -> {
                        oldPasswordTill.error = getString(R.string.password_is_incorrect)
                    }
                    !isValidPassword(password) -> {
                        passwordTill.error = getString(R.string.password_is_incorrect)
                    }
                    !(password == twoPassword) -> {
                        passwordTwoTill.error = getString(R.string.password_mismatch)
                    }
                    password == "" -> {
                        toast(getString(R.string.enter_password))
                    }
                    twoPassword == "" -> {
                        toast(getString(R.string.enter_your_password_a_second_time))
                    }
                    else -> {
                        dialog = ProgressDialog(this@UserSettingsActivity).apply {
                            setMessage(getString(R.string.please_wait))
                            setTitle(getString(R.string.loading_data))
                            show()
                            setCancelable(false)
                        }
                        try {
                            val autor = App.repository.changePassword(
                                PasswordChangeRequestDto(
                                    oldPassword,
                                    password
                                )
                            )
                            dialog?.dismiss()
                            if (autor.isSuccessful) {
                                toast(getString(R.string.password_change_successful))
                                passwordText.setText("")
                                passwordTwoText.setText("")
                                oldPasswordText.setText("")
                            } else {
                                toast(getString(R.string.incorrect_password))
                            }
                        } catch (e: Exception) {
                            toast(getString(R.string.falien_connect))
                        }
                    }
                }

            }
        }
        attachPhotoImgSetting.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun dispatchTakePictureIntent() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Загрузить фото из")
            .setPositiveButton(
                getString(R.string.camera),
                DialogInterface.OnClickListener { dialog, id -> //camera intent
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        takePictureIntent.resolveActivity(packageManager)?.also {
                            startActivityForResult(
                                takePictureIntent,
                                UserSettingsActivity.REQUEST_CAMERA
                            )
                        }
                    }
                })
            .setNegativeButton(
                getString(R.string.gallerea),
                DialogInterface.OnClickListener { dialog, id ->
                    val permissionStatus = ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )

                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        val loadIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(loadIntent, UserSettingsActivity.REQUEST_GALLERY)
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            UserSettingsActivity.REQUEST_GALLERY
                        )
                    }
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    companion object {
        const val REQUEST_CAMERA = 0
        const val REQUEST_GALLERY = 1

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            UserSettingsActivity.REQUEST_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val loadIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    startActivityForResult(loadIntent, UserSettingsActivity.REQUEST_GALLERY)
                } else {
                    toast(getString(R.string.permission_not_given))
                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        val imageBitmap: Bitmap? =
            when (requestCode) {
                UserSettingsActivity.REQUEST_CAMERA -> {
                    data.extras?.get("data") as Bitmap?
                }
                UserSettingsActivity.REQUEST_GALLERY -> {
                    data.data?.let { uri ->
                        contentResolver.openInputStream(uri).use {
                            BitmapFactory.decodeStream(it)
                        }
                    }
                }
                else -> {
                    null
                }
            }
        imageBitmap?.let {
            lifecycleScope.launch {
                dialog = ProgressDialog(this@UserSettingsActivity).apply {
                    setMessage(this@UserSettingsActivity.getString(R.string.please_wait))
                    setTitle(getString(R.string.set_img))
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                val imageUploadResult = App.repository.uploadUser(it)
                NotifictionHelper.mediaUploaded(AttachmentType.IMAGE, this@UserSettingsActivity)
                dialog?.dismiss()
                if (imageUploadResult.isSuccessful) {
                    imageUploaded()
                    attachmentModel = imageUploadResult.body()

                } else {
                    toast("Не удачная загрузка фото")
                }
            }
        }

    }

    private fun imageUploaded() {
        transparetAllIcons()
        attachPhotoDoneImgSetting.visibility = View.VISIBLE
    }

    private fun transparetAllIcons() {
        attachPhotoImgSetting.setImageResource(R.drawable.ic_image_inactive)
    }
}