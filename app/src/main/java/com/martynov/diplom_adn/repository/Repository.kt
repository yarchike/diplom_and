package com.martynov.diplom_adn.repository

import android.graphics.Bitmap
import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.CreateIdeaRequest
import com.martynov.diplom_adn.data.Token
import com.martynov.diplom_adn.model.IdeaModel
import retrofit2.Response

interface Repository {
    suspend fun authenticate(login: String, password: String): Response<Token>
    suspend fun uploadUser(bitmap: Bitmap): Response<AttachmentModel>
    suspend fun register(login: String, password: String, attachmentModel:AttachmentModel?): Response<Token>
    suspend fun upload(bitmap: Bitmap): Response<AttachmentModel>
    suspend fun createIdea(createIdeaRequest: CreateIdeaRequest):Response<Void>
    suspend fun getIdea(): Response<List<IdeaModel>>
    suspend fun like(id: Long):Response<IdeaModel>
    suspend fun disLike(id: Long):Response<IdeaModel>
}