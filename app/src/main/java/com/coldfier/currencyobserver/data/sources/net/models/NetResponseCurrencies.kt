package com.coldfier.currencyobserver.data.sources.net.models

import com.squareup.moshi.Json

data class NetResponseCurrencies(
    @Json(name = "symbols")
    val currencies: Map<String, String>? = null
)
