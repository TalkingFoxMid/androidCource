package com.talkingfox.composedtrabbithacker.data

import com.talkingfox.composedtrabbithacker.data.room.TokenDAO
import com.talkingfox.composedtrabbithacker.data.room.model.Token
import com.talkingfox.composedtrabbithacker.repository.TokenRepository

class TokenRepositoryImpl(private val dao: TokenDAO): TokenRepository {
    override fun fetchToken(): String? {
        val r = dao.getToken().map { it.token }.firstOrNull()
        println("GET: ${dao.getToken().map { it.id }}")
        return r
    }
    override fun updateToken(token: String): Unit {
        dao.setToken(Token(token))
    }
}