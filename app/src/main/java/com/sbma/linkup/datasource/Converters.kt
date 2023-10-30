package com.sbma.linkup.datasource

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID


/**
 * Convert Dates to string unix when saving to db and convert to Date when retrieving.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        if (value == null) {
            return null
        }

        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): UUID {
        return UUID.fromString(string)
    }

}
