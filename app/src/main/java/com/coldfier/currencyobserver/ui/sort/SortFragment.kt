package com.coldfier.currencyobserver.ui.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings
import com.coldfier.currencyobserver.databinding.FragmentSortBinding
import com.coldfier.currencyobserver.ui.base.BaseFragment

class SortFragment : BaseFragment<FragmentSortBinding, SortViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSortBinding
        get() = { inflater, container, attachToRoot ->
            FragmentSortBinding.inflate(inflater, container, attachToRoot)
        }

    override val viewModel: SortViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenStateFlow.observeWithLifecycle { renderState(it) }

        binding.chipAlphabetIncrease.setOnClickListener {
            viewModel.setSelectedSortSetting(SortSettings.ALPHABET_ASC)
        }

        binding.chipAlphabetDecrease.setOnClickListener {
            viewModel.setSelectedSortSetting(SortSettings.ALPHABET_DESC)

        }

        binding.chipValueIncrease.setOnClickListener {
            viewModel.setSelectedSortSetting(SortSettings.VALUE_ASC)

        }

        binding.chipValueDecrease.setOnClickListener {
            viewModel.setSelectedSortSetting(SortSettings.VALUE_DESC)

        }
    }

    private fun renderState(state: SortState) {
        binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        setSelectedSortSetting(state.chosenSetting)
    }

    private fun setSelectedSortSetting(sortSettings: SortSettings) {
        binding.chipAlphabetIncrease.isSelected = sortSettings == SortSettings.ALPHABET_ASC
        binding.chipAlphabetDecrease.isSelected = sortSettings == SortSettings.ALPHABET_DESC
        binding.chipValueIncrease.isSelected = sortSettings == SortSettings.VALUE_ASC
        binding.chipValueDecrease.isSelected = sortSettings == SortSettings.VALUE_DESC
    }
}