package com.coldfier.currencyobserver.ui.sort

import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings

data class SortState(
    val isLoading: Boolean,
    val chosenSetting: SortSettings
)