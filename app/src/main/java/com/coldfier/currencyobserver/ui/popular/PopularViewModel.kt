package com.coldfier.currencyobserver.ui.popular

import androidx.lifecycle.viewModelScope
import com.coldfier.currencyobserver.data.repositories.models.Currency
import com.coldfier.currencyobserver.domain.PopularUseCase
import com.coldfier.currencyobserver.ui.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

class PopularViewModel @Inject constructor(
    private val popularUseCase: PopularUseCase
) : BaseViewModel<PopularState, PopularSideEffect>() {

    override val _screenStateFlow = MutableStateFlow(PopularState.initialState)

    init {
        popularUseCase.allCurrenciesFlow
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
            .catch {  }
            .launchIn(viewModelScope)

        popularUseCase.sortSettingsFlow
            .onEach { sortSettings ->
                _screenStateFlow.update { it.copy(sortSettings = sortSettings) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadExchangePairs(currencyCode: String) {
        viewModelScope.safeLaunch(
            doAsync = {
                _screenStateFlow.update { it.copy(isLoading = true) }
                val pairs = popularUseCase.getCurrencyExchangePairs(currencyCode)
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
            popularUseCase.updateAllCurrencies()
            loadExchangePairs(_screenStateFlow.value.chosenCurrency.currencyCode)
        }
    }

    fun changeFavoriteCurrency(currencyCode: String) {
        viewModelScope.safeLaunch(
            onError = { e -> handleException(e) }
        ) {
            popularUseCase.changeFavoriteCurrency(currencyCode)
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is SocketTimeoutException -> {
                viewModelScope.launch {
                    sideEffectChannel.send(PopularSideEffect.ShowErrorNetworkDialog)
                }
            }

            else -> {
                viewModelScope.launch {
                    sideEffectChannel.send(PopularSideEffect.ShowUnknownErrorDialog)
                }
            }
        }
    }
}