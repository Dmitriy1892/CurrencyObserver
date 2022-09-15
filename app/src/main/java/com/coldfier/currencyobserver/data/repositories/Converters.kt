package com.coldfier.currencyobserver.data.repositories

import com.coldfier.currencyobserver.data.repositories.models.Currency
import com.coldfier.currencyobserver.data.repositories.models.CurrencyWithExchangePairs
import com.coldfier.currencyobserver.data.repositories.models.ExchangePair
import com.coldfier.currencyobserver.data.sources.db.models.DbCurrency
import com.coldfier.currencyobserver.data.sources.net.models.NetResponseExchanges
import java.util.*

fun DbCurrency.convertToCurrency(): Currency = Currency(
    name = this.name,
    currencyCode = this.currencyCode
)

fun NetResponseExchanges.convertToCurrencyWithExchangePairs(
    favoriteCurrencies: List<Currency>
): CurrencyWithExchangePairs {
    return CurrencyWithExchangePairs(
        currency = Currency(name = "", currencyCode = this.baseCurrencyCode ?: "ERROR"),
        date = this.date ?: Date(),
        exchangePairs = this.exchangeRates
            ?.convertToExchangePairs(favoriteCurrencies, baseCurrencyCode ?: "")
            ?: emptyList()
    )
}

fun Map<String, Double>.convertToExchangePairs(
    favoriteCurrencies: List<Currency>,
    baseCurrencyCode: String
): List<ExchangePair> {

    val list = mutableListOf<ExchangePair>()

    for ((currencyCode, value ) in this) {
        if (currencyCode == baseCurrencyCode) continue
        list.add(
            ExchangePair(
                currencyCode = currencyCode,
                value = value,
                isFavorite = favoriteCurrencies.any { it.currencyCode == currencyCode }
            )
        )
    }

    return list
}

fun Map<String, String>.convertToDbCurrencyList(): List<DbCurrency> {
    return this.map { (code, name) ->
        DbCurrency(
            currencyCode = code,
            name = name,
            isFavorite = false
        )
    }
}