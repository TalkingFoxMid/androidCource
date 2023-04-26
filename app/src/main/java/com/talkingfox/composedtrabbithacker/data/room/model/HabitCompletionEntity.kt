package com.talkingfox.composedtrabbithacker.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = params.habitCompletionTableName)
class HabitCompletionEntity {
    constructor() {

    }
    constructor(_uuid: UUID,
                _periodNumber: Long,
                _tries: Long) {
        habitUUID = _uuid
        periodNumber = _periodNumber
        tries = _tries
    }

    @PrimaryKey
    @ColumnInfo(name = "uuid")
    lateinit var habitUUID: UUID

    @ColumnInfo(name = "periodNumber")
    var periodNumber: Long = 0

    @ColumnInfo(name = "tries")
    var tries: Long = 0
}