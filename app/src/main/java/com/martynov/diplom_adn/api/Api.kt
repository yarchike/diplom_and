package com.martynov.diplom_adn.api

import com.martynov.diplom_adn.data.AttachmentModel
import com.martynov.diplom_adn.data.AuthRequestParams
import com.martynov.diplom_adn.data.Token
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface API {
        @POST("api/v1/authentication")
        suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>
        @Multipart
        @POST("api/v1/media/user")
        suspend fun uploadImageUser(@Part file: MultipartBody.Part):
                Response<AttachmentModel>
}