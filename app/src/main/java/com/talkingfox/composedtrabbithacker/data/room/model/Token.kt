package com.talkingfox.composedtrabbithacker.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token")
class Token {
    constructor() {

    }

    constructor(_token: String) {
        token = _token
    }

    @ColumnInfo(name = "token")
    lateinit var token: String

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0
}