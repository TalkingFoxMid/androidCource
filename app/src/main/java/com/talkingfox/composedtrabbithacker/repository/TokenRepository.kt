package com.talkingfox.composedtrabbithacker.repository

interface TokenRepository {
    fun fetchToken(): String?
    fun updateToken(token: String): Unit
}