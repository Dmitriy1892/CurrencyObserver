package com.coldfier.currencyobserver.ui.favorites

import androidx.lifecycle.viewModelScope
import com.coldfier.currencyobserver.data.repositories.models.Currency
import com.coldfier.currencyobserver.domain.FavoritesUseCase
import com.coldfier.currencyobserver.ui.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase
) : BaseViewModel<FavoritesState, FavoritesSideEffect>() {

    override val _screenStateFlow = MutableStateFlow(FavoritesState.initialState)

    init {
        favoritesUseCase.favoriteCurrenciesFlow
            .onStart {
                _screenStateFlow.update { it.copy(isLoading = true) }
            }
            .onEach { currencies ->
                _screenStateFlow.update {
                    it.copy(
                        currencies = currencies,
                        chosenCurrency = if (it.chosenCurrency.currencyCode == "") {
                            currencies.firstOrNull() ?: it.chosenCurrency
                        } else { it.chosenCurrency }
                    )
                }
                loadExchangePairs(_screenStateFlow.value.chosenCurrency.currencyCode)
            }
            .launchIn(viewModelScope)

        favoritesUseCase.sortSettingsFlow
            .onEach { sortSettings ->
                _screenStateFlow.update { it.copy(sortSettings = sortSettings) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadExchangePairs(currencyCode: String) {
        viewModelScope.safeLaunch(
            doAsync = {
                _screenStateFlow.update { it.copy(isLoading = true) }
                val pairs = favoritesUseCase.getCurrencyExchangePairs(
                    currencyCode,
                    _screenStateFlow.value.currencies.map { it.currencyCode }
                )
                _screenStateFlow.update {
                    it.copy(
                        isLoading = false,
                        exchangePairs = pairs
                    )
                }
            },
            onError = { e ->
                _screenStateFlow.update { it.copy(isLoading = false) }
                handleException(e)
            }
        )
    }

    fun setChosenCurrency(currency: Currency) {
        _screenStateFlow.update { it.copy(chosenCurrency = currency) }
        loadExchangePairs(currency.currencyCode)
    }

    fun updateScreenInfo() {
        viewModelScope.safeLaunch(
            onError = { e -> handleException(e) }
        ) {
            favoritesUseCase.updateAllCurrencies()
            loadExchangePairs(_screenStateFlow.value.chosenCurrency.currencyCode)
        }
    }

    fun removeFavoriteCurrency(currencyCode: String) {
        viewModelScope.safeLaunch(
            onError = { e -> handleException(e) }
        ) {
            favoritesUseCase.removeCurrencyFromFavorite(currencyCode)
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is SocketTimeoutException -> {
                viewModelScope.launch {
                    sideEffectChannel.send(FavoritesSideEffect.ShowErrorNetworkDialog)
                }
            }

            else -> {
                viewModelScope.launch {
                    sideEffectChannel.send(FavoritesSideEffect.ShowUnknownErrorDialog)
                }
            }
        }
    }
}