package com.martynov.diplom_adn.repository

import com.martynov.diplom_adn.api.API
import com.martynov.diplom_adn.data.AuthRequestParams
import com.martynov.diplom_adn.data.Token
import retrofit2.Response

class NetworkRepository(private val api: API) : Repository {
    private var token: String? = null
    override suspend fun authenticate(login: String, password: String): Response<Token> {
        token = api.authenticate(AuthRequestParams(username = login, password = password)).body()?.token
        return api.authenticate(AuthRequestParams(username = login, password = password))
    }
}