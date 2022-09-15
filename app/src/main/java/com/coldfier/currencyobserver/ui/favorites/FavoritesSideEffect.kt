package com.coldfier.currencyobserver.ui.favorites

sealed interface FavoritesSideEffect {
    object ShowErrorNetworkDialog : FavoritesSideEffect
    object ShowUnknownErrorDialog : FavoritesSideEffect
}