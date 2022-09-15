package com.coldfier.currencyobserver.data.sources.db

import androidx.room.TypeConverter
import java.util.*

class TimestampToDateTypeConverter {
    @TypeConverter
    fun fromTimestamp(timestampSec: Long): Date = Date(timestampSec * 1000)

    @TypeConverter
    fun toTimestamp(date: Date): Long = date.time / 1000
}