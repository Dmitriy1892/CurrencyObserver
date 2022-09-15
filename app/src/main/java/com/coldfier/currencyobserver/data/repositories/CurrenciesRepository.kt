package com.coldfier.currencyobserver.data.repositories

import com.coldfier.currencyobserver.data.repositories.models.Currency
import com.coldfier.currencyobserver.data.repositories.models.CurrencyWithExchangePairs
import com.coldfier.currencyobserver.data.repositories.models.ExchangePair
import com.coldfier.currencyobserver.data.sources.db.dao.CurrenciesDao
import com.coldfier.currencyobserver.data.sources.net.api.CurrenciesApi
import com.coldfier.currencyobserver.data.sources.shared_prefs.SortSettingsDataStore
import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrenciesRepository @Inject constructor(
    private val currenciesApi: CurrenciesApi,
    private val currenciesDao: CurrenciesDao,
    private val sortSettingsDataStore: SortSettingsDataStore
) {

    val sortSettingsFlow = sortSettingsDataStore.sortSettingsFlow
        .flowOn(Dispatchers.IO)

    /**
     * [favoriteCurrenciesFlow] used for displaying currencies in picker on Favorites screen
     */
    val favoriteCurrenciesFlow: Flow<List<Currency>> = currenciesDao.getFavoriteCurrencies()
        .distinctUntilChanged()
        .map { dbCurrencies ->
            dbCurrencies.map { it.convertToCurrency() }
        }
        .combine(sortSettingsFlow) { currencies, _ ->
            currencies.sortBySettings()
        }
        .flowOn(Dispatchers.IO)

    /**
     * [allCurrenciesFlow] used for displaying currencies in picker on Popular screen
     */
    val allCurrenciesFlow: Flow<List<Currency>> = currenciesDao.getAllCurrencies()
        .distinctUntilChanged()
        .map { dbCurrencies ->
            dbCurrencies.map { it.convertToCurrency() }
        }
        .combine(sortSettingsFlow) { currencies, _ ->
            currencies.sortBySettings()
        }
        .flowOn(Dispatchers.IO)

    suspend fun getCurrencyExchangePairs(
        currencyCode: String,
        rateCodes: List<String>? = null
    ): CurrencyWithExchangePairs {
        return withContext(Dispatchers.IO) {
            val favoriteCurrencies = favoriteCurrenciesFlow.firstOrNull() ?: emptyList()
            val rateCodesString = rateCodes?.let {
                var str = ""
                for (i in it) {
                    str += "$i,"
                }

                str.substringBeforeLast(",")
            }
            currenciesApi.getCurrencyExchangePairs(currencyCode, rateCodesString)
                .convertToCurrencyWithExchangePairs(favoriteCurrencies)
                .sortBySettings()
        }
    }

    suspend fun updateAllCurrencies() {
        withContext(Dispatchers.IO) {
            val newCurrencies = currenciesApi.getCurrencies().currencies
                ?.convertToDbCurrencyList()
                ?: emptyList()

            currenciesDao.saveCurrencies(newCurrencies)
        }
    }

    suspend fun isCurrencyFavorite(currencyCode: String): Boolean {
        return withContext(Dispatchers.IO) {
            currenciesDao.isCurrencyFavorite(currencyCode)
        }
    }

    suspend fun addCurrencyToFavorite(currencyCode: String) {
        withContext(Dispatchers.IO) {
            currenciesDao.updateCurrencyFavoriteFlag(currencyCode, true)
        }
    }

    suspend fun removeCurrencyFromFavorite(currencyCode: String) {
        withContext(Dispatchers.IO) {
            currenciesDao.updateCurrencyFavoriteFlag(currencyCode, false)
        }
    }

    suspend fun saveSortSettings(sortSettings: SortSettings) {
        withContext(Dispatchers.IO) {
            sortSettingsDataStore.saveSettings(sortSettings)
        }
    }

    private suspend fun List<Currency>.sortBySettings(): List<Currency> {
        return when (sortSettingsFlow.first()) {
            SortSettings.NONE -> this
            SortSettings.ALPHABET_ASC -> this.sortedBy { it.currencyCode }
            SortSettings.ALPHABET_DESC -> this.sortedByDescending { it.currencyCode }
            SortSettings.VALUE_ASC -> this
            SortSettings.VALUE_DESC -> this
        }
    }

    private suspend fun CurrencyWithExchangePairs.sortBySettings(): CurrencyWithExchangePairs {
        return this.copy(exchangePairs = this.exchangePairs.sortedBySettings())
    }

    private suspend fun List<ExchangePair>.sortedBySettings(): List<ExchangePair> {
        return when (sortSettingsFlow.first()) {
            SortSettings.NONE -> this
            SortSettings.ALPHABET_ASC -> this.sortedBy { it.currencyCode }
            SortSettings.ALPHABET_DESC -> this.sortedByDescending { it.currencyCode }
            SortSettings.VALUE_ASC -> this.sortedBy { it.value }
            SortSettings.VALUE_DESC -> this.sortedByDescending { it.value }
        }
    }
}