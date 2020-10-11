package com.martynov.diplom_adn

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.AttachmentType
import com.martynov.diplom_adn.data.CreateIdeaRequest
import kotlinx.android.synthetic.main.activity_create_idea.*
import kotlinx.coroutines.launch
import splitties.toast.toast
import java.util.*

class CreateIdeaActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var attachmentModel: AttachmentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_idea)

        attachPhotoImg.setOnClickListener {
            dispatchTakePictureIntent()
        }
        createIdeaBtn.setOnClickListener {
            lifecycleScope.launch {
                dialog = ProgressDialog(this@CreateIdeaActivity).apply {
                    setMessage(getString(R.string.please_wait))
                    setTitle(getString(R.string.creat_new_idea))
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                try {
                    val date = Calendar.getInstance().timeInMillis
                    val result = App.repository.createIdea(
                        CreateIdeaRequest(
                            ideaText = contentEdt.text.toString(), date = date,
                            attachment = attachmentModel,
                            like = 0,
                            disLike = 0
                        )
                    )
                    if (result.isSuccessful) {
                        handleSuccessfullResult()
                    } else {
                        handleFailedResult()
                    }
                } catch (e: Exception) {
                    handleFailedResult()
                } finally {
                    dialog?.dismiss()
                }
            }
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
                                RegistrationActivity.REQUEST_CAMERA
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

                        startActivityForResult(loadIntent, RegistrationActivity.REQUEST_GALLERY)
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_GALLERY
                        )
                    }
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data == null) {
            return
        }
        val imageBitmap: Bitmap? =
            when (requestCode) {
                RegistrationActivity.REQUEST_CAMERA -> {
                    data.extras?.get("data") as Bitmap?
                }
                RegistrationActivity.REQUEST_GALLERY -> {
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
                dialog = ProgressDialog(this@CreateIdeaActivity).apply {
                    setMessage(this@CreateIdeaActivity.getString(R.string.please_wait))
                    setTitle("Отправка изображения")
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                val imageUploadResult = App.repository.upload(it)
                NotifictionHelper.mediaUploaded(AttachmentType.IMAGE, this@CreateIdeaActivity)
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
        attachPhotoDoneImg.visibility = View.VISIBLE
    }

    private fun transparetAllIcons() {
        attachPhotoImg.setImageResource(R.drawable.ic_image_inactive)
    }

    companion object {
        const val REQUEST_CAMERA = 0
        const val REQUEST_GALLERY = 1
    }

    private fun handleSuccessfullResult() {
        toast(R.string.idea_created_successfully)
        finish()
    }

    private fun handleFailedResult() {
        toast(R.string.error_occured)
    }
}