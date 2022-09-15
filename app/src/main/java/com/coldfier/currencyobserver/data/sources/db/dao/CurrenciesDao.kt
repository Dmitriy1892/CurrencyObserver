package com.coldfier.currencyobserver.data.sources.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coldfier.currencyobserver.data.sources.db.models.DbCurrency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrenciesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveCurrencies(currencies: List<DbCurrency>)

    @Query("SELECT * FROM dbcurrency WHERE isFavorite = 1")
    fun getFavoriteCurrencies(): Flow<List<DbCurrency>>

    @Query("SELECT * FROM dbcurrency")
    fun getAllCurrencies(): Flow<List<DbCurrency>>

    @Query("UPDATE dbcurrency SET isFavorite = :isFavorite WHERE currencyCode = :currencyCode")
    suspend fun updateCurrencyFavoriteFlag(currencyCode: String, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM dbcurrency WHERE currencyCode = :currencyCode")
    suspend fun isCurrencyFavorite(currencyCode: String): Boolean
}