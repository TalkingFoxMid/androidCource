package com.talkingfox.composedtrabbithacker.data.room.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*

data class HabitCompletionEntityWithPeriod(
    val uuid: UUID,
    val periodNumber: Long = 0,
    val tries: Long = 0,
    val periodDays: Long = 0
)