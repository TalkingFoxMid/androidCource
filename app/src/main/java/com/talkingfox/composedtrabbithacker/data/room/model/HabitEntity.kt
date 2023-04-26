package com.talkingfox.composedtrabbithacker.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = params.habitsDataTableName)
class HabitEntity {
    constructor() {

    }
    constructor(_uuid: UUID,
                _name: String,
                _description: String,
                _priority: String,
                _type: String,
                _periodDays: Int,
                _retries: Int,
                _creationDate: Long) {
        uuid = _uuid
        name = _name
        description = _description
        priority = _priority
        type = _type
        periodDays = _periodDays
        retries = _retries
        creationDate = _creationDate
    }
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    lateinit var uuid: UUID

    @ColumnInfo(name = "name")
    lateinit var name: String

    @ColumnInfo(name = "description")
    lateinit var description: String

    @ColumnInfo(name = "priority")
    lateinit var priority: String

    @ColumnInfo(name = "type")
    lateinit var type: String

    @ColumnInfo(name = "periodDays")
    var periodDays: Int = 0

    @ColumnInfo(name = "retries")
    var retries: Int = 0

    @ColumnInfo(name = "date")
    var creationDate: Long = 0

}