package com.coldfier.currencyobserver.data.sources.net

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class TimestampToDateAdapter {
    @FromJson
    fun fromJson(timestampSec: Long?): Date? {
        return timestampSec?.let { Date(it * 1000) }
    }

    @ToJson
    fun toJson(date: Date): Long {
        return date.time / 1000
    }
}