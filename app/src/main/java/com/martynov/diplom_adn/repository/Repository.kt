package com.martynov.diplom_adn.repository

import android.graphics.Bitmap
import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.Token
import retrofit2.Response

interface Repository {
    suspend fun authenticate(login: String, password: String): Response<Token>
    suspend fun uploadUser(bitmap: Bitmap): Response<AttachmentModel>
    suspend fun register(login: String, password: String, attachmentModel:AttachmentModel?): Response<Token>
}