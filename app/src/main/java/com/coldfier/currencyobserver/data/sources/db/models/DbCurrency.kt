package com.coldfier.currencyobserver.data.sources.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbCurrency(
    @PrimaryKey(autoGenerate = false)
    val currencyCode: String,
    val name: String,
    val isFavorite: Boolean
)