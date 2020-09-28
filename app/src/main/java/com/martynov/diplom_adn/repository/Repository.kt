package com.martynov.diplom_adn.repository

import com.martynov.diplom_adn.data.Token
import retrofit2.Response

interface Repository {
    suspend fun authenticate(login: String, password: String): Response<Token>
}