package com.coldfier.currencyobserver.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<State: Any, SideEffect: Any> : ViewModel() {

    protected abstract val _screenStateFlow: MutableStateFlow<State>
    val screenStateFlow: StateFlow<State>
        get() = _screenStateFlow.asStateFlow()

    protected val sideEffectChannel = Channel<SideEffect>()
    val sideEffectFlow: Flow<SideEffect>
        get() = sideEffectChannel.receiveAsFlow().flowOn(Dispatchers.Main.immediate)

    protected fun CoroutineScope.safeLaunch(
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        onError: suspend (e: Exception) -> Unit = {},
        doAsync: suspend () -> Unit
    ) {
        this.launch(dispatcher) {
            try {
                doAsync()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}