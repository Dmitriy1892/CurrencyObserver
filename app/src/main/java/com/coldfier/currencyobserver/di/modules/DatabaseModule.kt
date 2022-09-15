package com.coldfier.currencyobserver.di.modules

import android.content.Context
import androidx.room.Room
import com.coldfier.currencyobserver.data.sources.db.CurrenciesDatabase
import com.coldfier.currencyobserver.data.sources.db.dao.CurrenciesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideCurrenciesDatabase(context: Context): CurrenciesDatabase =
        Room.databaseBuilder(context, CurrenciesDatabase::class.java, "room_currencies_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCurrenciesDao(currenciesDatabase: CurrenciesDatabase): CurrenciesDao =
        currenciesDatabase.getCurrenciesDao()
}