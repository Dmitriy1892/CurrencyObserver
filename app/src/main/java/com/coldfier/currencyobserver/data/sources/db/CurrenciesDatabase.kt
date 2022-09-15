package com.coldfier.currencyobserver.data.sources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coldfier.currencyobserver.data.sources.db.dao.CurrenciesDao
import com.coldfier.currencyobserver.data.sources.db.models.DbCurrency

@Database(
    entities = [DbCurrency::class],
    version = 1
)
@TypeConverters(TimestampToDateTypeConverter::class)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract fun getCurrenciesDao(): CurrenciesDao
}