package com.martynov.diplom_adn

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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
                                Companion.REQUEST_IMAGE_CAPTURE
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

                    startActivityForResult(loadIntent, REQUEST_IMAGE_CAPTURE)
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

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                lifecycleScope.launch {
                    dialog = ProgressDialog(this@RegistrationActivity).apply {
                        setMessage(this@RegistrationActivity.getString(R.string.please_wait))
                        setTitle(R.string.registration)
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
                        toast("Can't upload image")
                    }
                }
            }
        }

    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}