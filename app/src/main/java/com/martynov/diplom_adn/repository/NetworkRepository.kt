package com.martynov.diplom_adn.repository

import android.graphics.Bitmap
import com.martynov.diplom_adn.api.API
import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.AuthRequestParams
import com.martynov.diplom_adn.data.RegistrationRequestParams
import com.martynov.diplom_adn.data.Token
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream

class NetworkRepository(private val api: API) : Repository {
    private var token: String? = null
    override suspend fun authenticate(login: String, password: String): Response<Token> {
        token = api.authenticate(AuthRequestParams(username = login, password = password)).body()?.token
        return api.authenticate(AuthRequestParams(username = login, password = password))
    }

    override suspend fun uploadUser(bitmap: Bitmap): Response<AttachmentModel> {

        val bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle =

            RequestBody.create("image/jpeg".toMediaTypeOrNull(), bos.toByteArray())
        val body =

            MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        return api.uploadImageUser(body)
    }

    override suspend fun register(login: String, password: String,attachmentModel: AttachmentModel?): Response<Token> =
        api.register(RegistrationRequestParams(username = login, password = password, attachmentModel = attachmentModel))


}