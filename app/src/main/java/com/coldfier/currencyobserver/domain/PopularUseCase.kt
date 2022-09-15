package com.coldfier.currencyobserver.domain

import com.coldfier.currencyobserver.data.repositories.CurrenciesRepository
import com.coldfier.currencyobserver.data.repositories.models.CurrencyWithExchangePairs
import javax.inject.Inject

class PopularUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository
) {

    val allCurrenciesFlow = currenciesRepository.allCurrenciesFlow

    val sortSettingsFlow = currenciesRepository.sortSettingsFlow

    suspend fun getCurrencyExchangePairs(currencyCode: String): CurrencyWithExchangePairs {
        return currenciesRepository.getCurrencyExchangePairs(currencyCode)
    }

    suspend fun updateAllCurrencies() {
        currenciesRepository.updateAllCurrencies()
    }

    suspend fun changeFavoriteCurrency(currencyCode: String) {
        val isFavorite = currenciesRepository.isCurrencyFavorite(currencyCode)

        if (isFavorite) currenciesRepository.removeCurrencyFromFavorite(currencyCode)
        else currenciesRepository.addCurrencyToFavorite(currencyCode)
    }
}