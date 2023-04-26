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
                _date: Long) {
        habitUUID = _uuid
        completeDate = _date
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "uuid")
    lateinit var habitUUID: UUID

    @ColumnInfo(name = "date")
    var completeDate: Long = 0
}