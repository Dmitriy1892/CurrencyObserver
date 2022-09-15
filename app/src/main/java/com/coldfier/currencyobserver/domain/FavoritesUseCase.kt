package com.coldfier.currencyobserver.domain

import com.coldfier.currencyobserver.data.repositories.CurrenciesRepository
import com.coldfier.currencyobserver.data.repositories.models.CurrencyWithExchangePairs
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository
) {

    val favoriteCurrenciesFlow = currenciesRepository.favoriteCurrenciesFlow

    val sortSettingsFlow = currenciesRepository.sortSettingsFlow

    suspend fun getCurrencyExchangePairs(
        currencyCode: String,
        rateCodes: List<String>
    ): CurrencyWithExchangePairs {
        return currenciesRepository.getCurrencyExchangePairs(currencyCode, rateCodes)
    }

    suspend fun updateAllCurrencies() {
        currenciesRepository.updateAllCurrencies()
    }

    suspend fun removeCurrencyFromFavorite(currencyCode: String) {
        return currenciesRepository.removeCurrencyFromFavorite(currencyCode)
    }
}