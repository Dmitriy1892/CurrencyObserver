package com.coldfier.currencyobserver.data.sources.net.models

import com.squareup.moshi.Json
import java.util.*

data class NetResponseExchanges(
    @Json(name = "base")
    val baseCurrencyCode: String? = null,
    @Json(name = "rates")
    val exchangeRates: Map<String, Double>? = null,
    @Json(name = "timestamp")
    val date: Date? = null
)
