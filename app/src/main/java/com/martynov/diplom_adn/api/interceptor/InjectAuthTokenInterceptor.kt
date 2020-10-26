package com.martynov.diplom_adn.api.interceptor

import com.martynov.diplom_adn.AUTH_TOKEN_HEADER
import okhttp3.Interceptor
import okhttp3.Response

class InjectAuthTokenInterceptor(val authToken: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithToken = originalRequest.newBuilder()
            .header(AUTH_TOKEN_HEADER, "Bearer ${authToken()}")
            .build()
        return chain.proceed(requestWithToken)
    }

}