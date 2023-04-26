package com.talkingfox.composedtrabbithacker.data.room

import androidx.room.*
import com.talkingfox.composedtrabbithacker.data.room.model.TokenEntity

@Dao
interface TokenDAO {

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun setToken(tokenEntity: TokenEntity)

 @Query("SELECT * from token")
 fun getToken(): List<TokenEntity>

}