package com.martynov.diplom_adn

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.AttachmentType
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.launch
import splitties.toast.toast


class RegistrationActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var attachmentModel: AttachmentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        title = getString(R.string.registration)

        attachPhotoImg.setOnClickListener {
            dispatchTakePictureIntent()

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
                                Companion.REQUEST_CAMERA
                            )
                        }
                    }
                })
            .setNegativeButton(
                getString(R.string.gallerea),
                DialogInterface.OnClickListener { dialog, id ->
                    val loadIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    startActivityForResult(loadIntent, REQUEST_GALLERY)
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun imageUploaded() {
        transparetAllIcons()
        attachPhotoDoneImg.visibility = View.VISIBLE
    }

    private fun transparetAllIcons() {
        attachPhotoImg.setImageResource(R.drawable.ic_image_inactive)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        val imageBitmap: Bitmap? =
            when (requestCode) {
                REQUEST_CAMERA -> {
                    data.extras?.get("data") as Bitmap?
                }
                REQUEST_GALLERY -> {
                    data.data?.let { uri ->
                        contentResolver.openFileDescriptor(
                            uri,
                            "r"
                        )?.use {
                            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
                        }
                    }
                }
                else -> {
                    null
                }
            }
        imageBitmap?.let {
            lifecycleScope.launch {
                dialog = ProgressDialog(this@RegistrationActivity).apply {
                    setMessage(this@RegistrationActivity.getString(R.string.please_wait))
                    setTitle("Отправка изображения")
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                val imageUploadResult = App.repository.uploadUser(it)
                NotifictionHelper.mediaUploaded(AttachmentType.IMAGE, this@RegistrationActivity)
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

    companion object {
        const val REQUEST_CAMERA = 0
        const val REQUEST_GALLERY = 1

    }
}