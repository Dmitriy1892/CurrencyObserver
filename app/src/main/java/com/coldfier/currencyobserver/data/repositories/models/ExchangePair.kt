package com.coldfier.currencyobserver.data.repositories.models

data class ExchangePair(
    val currencyCode: String,
    val value: Double,
    val isFavorite: Boolean
)