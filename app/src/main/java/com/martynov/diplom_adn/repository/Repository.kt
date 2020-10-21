package com.martynov.diplom_adn.repository

import android.graphics.Bitmap
import com.martynov.diplom_adn.data.*
import com.martynov.diplom_adn.model.IdeaModel
import retrofit2.Response

interface Repository {
    suspend fun authenticate(login: String, password: String): Response<Token>
    suspend fun uploadUser(bitmap: Bitmap): Response<AttachmentModel>
    suspend fun register(
        login: String,
        password: String,
        attachmentModel: AttachmentModel?
    ): Response<Token>

    suspend fun upload(bitmap: Bitmap): Response<AttachmentModel>
    suspend fun createIdea(createIdeaRequest: CreateIdeaRequest): Response<Void>
    suspend fun getIdea(): Response<List<IdeaModel>>
    suspend fun like(id: Long): Response<IdeaModel>
    suspend fun disLike(id: Long): Response<IdeaModel>
    suspend fun getMe(): Response<AutorIdeaRequest>
    suspend fun changePassword(passwordChangeRequestDto: PasswordChangeRequestDto): Response<AutorIdeaRequest>
    suspend fun changeImg(attachmentModel: AttachmentModel):Response<Boolean>
    suspend fun getIdeaCount(idEndIdea: Long): Response<List<IdeaModel>>
    suspend fun registerPushToken(token: String, id:Long?): Response<AutorIdeaRequest>
    suspend fun getIdeaId(id: Long): Response<IdeaModel>
}