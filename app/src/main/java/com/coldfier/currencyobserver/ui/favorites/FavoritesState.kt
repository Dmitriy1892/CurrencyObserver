package com.coldfier.currencyobserver.ui.favorites

import com.coldfier.currencyobserver.data.repositories.models.Currency
import com.coldfier.currencyobserver.data.repositories.models.CurrencyWithExchangePairs
import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings
import java.util.*

data class FavoritesState(
    val isLoading: Boolean,
    val currencies: List<Currency>,
    val chosenCurrency: Currency,
    val exchangePairs: CurrencyWithExchangePairs,
    val sortSettings: SortSettings
) {
    companion object {
        val initialState = FavoritesState(
            isLoading = false,
            currencies = emptyList(),
            chosenCurrency = Currency(name = "", currencyCode = ""),
            exchangePairs = CurrencyWithExchangePairs(
                currency = Currency(name = "", currencyCode = ""),
                date = Date(),
                exchangePairs = emptyList()
            ),
            sortSettings = SortSettings.NONE
        )
    }
}
