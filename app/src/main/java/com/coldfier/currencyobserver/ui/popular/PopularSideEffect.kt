package com.coldfier.currencyobserver.ui.popular

sealed interface PopularSideEffect {
    object ShowErrorNetworkDialog : PopularSideEffect
    object ShowUnknownErrorDialog : PopularSideEffect
}