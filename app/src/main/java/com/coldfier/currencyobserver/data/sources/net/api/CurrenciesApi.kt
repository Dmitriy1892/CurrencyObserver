package com.coldfier.currencyobserver.data.sources.net.api

import com.coldfier.currencyobserver.data.sources.net.models.NetResponseCurrencies
import com.coldfier.currencyobserver.data.sources.net.models.NetResponseExchanges
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApi {

    @GET("/exchangerates_data/symbols")
    suspend fun getCurrencies(): NetResponseCurrencies

    @GET("/exchangerates_data/latest")
    suspend fun getCurrencyExchangePairs(
        @Query("base") currencyCode: String,
        @Query("symbols") rateCodes: String?
    ): NetResponseExchanges
}