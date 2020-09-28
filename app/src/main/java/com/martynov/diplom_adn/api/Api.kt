package com.martynov.diplom_adn.api

import com.martynov.diplom_adn.data.AuthRequestParams
import com.martynov.diplom_adn.data.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
        @POST("api/v1/authentication")
        suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

}