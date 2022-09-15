package com.coldfier.currencyobserver.ui.sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings
import com.coldfier.currencyobserver.domain.SortUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SortViewModel @Inject constructor(
    private val sortUseCase: SortUseCase
) : ViewModel() {

    private val _screenStateFlow = MutableStateFlow(
        SortState(
            isLoading = false,
            chosenSetting = SortSettings.NONE
        )
    )
    val screenStateFlow: StateFlow<SortState>
        get() = _screenStateFlow.asStateFlow()

    init {
        sortUseCase.sortSettingsFlow
            .onStart {
                _screenStateFlow.update { it.copy(isLoading = true) }
            }
            .onEach { sortSetting ->
                _screenStateFlow.update {
                    it.copy(
                        isLoading = false,
                        chosenSetting = sortSetting
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun setSelectedSortSetting(sortSettings: SortSettings) {
        viewModelScope.launch {
            sortUseCase.saveSortSettings(sortSettings)
        }
    }
}