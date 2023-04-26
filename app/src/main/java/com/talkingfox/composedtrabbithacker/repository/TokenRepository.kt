package com.talkingfox.composedtrabbithacker.repository

import com.talkingfox.composedtrabbithacker.data.room.model.Token

interface TokenRepository {
    fun fetchToken(): String?
    fun updateToken(token: String): Unit
}