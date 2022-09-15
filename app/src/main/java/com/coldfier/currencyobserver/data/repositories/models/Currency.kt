package com.coldfier.currencyobserver.data.repositories.models

data class Currency(
    val name: String,
    val currencyCode: String
) {
    override fun toString(): String {
        return currencyCode
    }
}