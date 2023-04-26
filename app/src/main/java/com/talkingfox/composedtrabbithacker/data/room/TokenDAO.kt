package com.talkingfox.composedtrabbithacker.data.room

import androidx.room.*
import com.talkingfox.composedtrabbithacker.data.room.model.Habit
import com.talkingfox.composedtrabbithacker.data.room.model.Token

@Dao
interface TokenDAO {

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun setToken(token: Token)

 @Query("SELECT * from token")
 fun getToken(): List<Token>

}