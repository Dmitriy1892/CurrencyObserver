package com.coldfier.currencyobserver.domain

import com.coldfier.currencyobserver.data.repositories.CurrenciesRepository
import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings
import javax.inject.Inject

class SortUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository
) {

    val sortSettingsFlow = currenciesRepository.sortSettingsFlow

    suspend fun saveSortSettings(sortSettings: SortSettings) =
        currenciesRepository.saveSortSettings(sortSettings)
}