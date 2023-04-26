package com.talkingfox.composedtrabbithacker.usecases

import com.talkingfox.composedtrabbithacker.repository.TokenRepository

interface TokenUseCases {
    fun getToken(): String?
    fun setToken(token: String): Unit
}

class TokenUseCasesImpl(private val tokenRepository: TokenRepository): TokenUseCases {
    override fun getToken(): String? = tokenRepository.fetchToken()
    override fun setToken(token: String): Unit = tokenRepository.updateToken(token)
}