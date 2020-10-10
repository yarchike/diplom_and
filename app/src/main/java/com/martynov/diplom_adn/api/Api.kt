package com.martynov.diplom_adn.api

import com.martynov.diplom_adn.data.*
import com.martynov.diplom_adn.model.IdeaModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface API {
        @POST("api/v1/authentication")
        suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>
        @Multipart
        @POST("api/v1/media/user")
        suspend fun uploadImageUser(@Part file: MultipartBody.Part):
                Response<AttachmentModel>
        @POST("api/v1/registration")
        suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>
        @Multipart
        @POST("api/v1/media")
        suspend fun uploadImage(@Part file: MultipartBody.Part):Response<AttachmentModel>
        @POST("api/v1/ideas/new")
        suspend fun createPost(@Body createIdeaRequest: CreateIdeaRequest): Response<Void>
        @GET("api/v1/ideas")
        suspend fun getIdea():Response<List<IdeaModel>>
}