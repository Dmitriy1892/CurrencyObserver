package com.coldfier.currencyobserver.data.repositories.models

import java.util.*

data class CurrencyWithExchangePairs(
    val currency: Currency,
    val date: Date,
    val exchangePairs: List<ExchangePair>
)