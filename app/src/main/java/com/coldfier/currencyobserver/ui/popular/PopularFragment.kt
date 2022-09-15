package com.coldfier.currencyobserver.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.coldfier.currencyobserver.R
import com.coldfier.currencyobserver.data.repositories.models.Currency
import com.coldfier.currencyobserver.data.sources.shared_prefs.models.SortSettings
import com.coldfier.currencyobserver.databinding.FragmentCurrencyPairsBinding
import com.coldfier.currencyobserver.ui.ChosenCurrencyAdapter
import com.coldfier.currencyobserver.ui.ExchangeRatesAdapter
import com.coldfier.currencyobserver.ui.ExchangeRatesItemDecorator
import com.coldfier.currencyobserver.ui.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class PopularFragment : BaseFragment<FragmentCurrencyPairsBinding, PopularViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCurrencyPairsBinding
        get() = { inflater, container, attachToRoot ->
            FragmentCurrencyPairsBinding.inflate(inflater, container, attachToRoot)
        }

    override val viewModel: PopularViewModel by viewModels { viewModelFactory }

    private var chosenCurrencyAdapter: ChosenCurrencyAdapter? = null

    private var exchangeRatesAdapter: ExchangeRatesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initClickers()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chosenCurrencyAdapter = null
        exchangeRatesAdapter = null
    }

    private fun initViews() {
        chosenCurrencyAdapter = ChosenCurrencyAdapter(requireContext())
        binding.menuSelectedCurrency.setAdapter(chosenCurrencyAdapter)

        exchangeRatesAdapter = ExchangeRatesAdapter { exchangePair ->
            viewModel.changeFavoriteCurrency(exchangePair.currencyCode)
        }
        binding.rvExchangeRates.apply {
            adapter = exchangeRatesAdapter
            addItemDecoration(ExchangeRatesItemDecorator())
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun initClickers() {
        binding.menuSelectedCurrency.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                chosenCurrencyAdapter?.getItem(position)?.let { currency ->
                    if (viewModel.screenStateFlow.value.chosenCurrency != currency) {
                        viewModel.setChosenCurrency(currency)
                    }
                }
            }

        binding.buttonSort.setOnClickListener {
            findNavController().navigate(R.id.action_global_sortFragment)
        }

        binding.buttonUpdate.setOnClickListener {
            viewModel.updateScreenInfo()
        }
    }

    private fun initObservers() {
        viewModel.screenStateFlow.observeWithLifecycle { renderState(it) }
        viewModel.sideEffectFlow.observeWithLifecycle { renderSideEffect(it) }
    }

    private fun renderState(state: PopularState) {

        binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        renderCurrencySelector(state.currencies, state.chosenCurrency)
        renderSortInfo(state.sortSettings)
        renderExchangeUpdateTime(state.exchangePairs.date)

        exchangeRatesAdapter?.submitList(state.exchangePairs.exchangePairs)
    }

    private fun renderSideEffect(effect: PopularSideEffect) {
        when (effect) {
            is PopularSideEffect.ShowErrorNetworkDialog -> showAlertDialog(R.string.check_network)

            is PopularSideEffect.ShowUnknownErrorDialog ->
                showAlertDialog(R.string.unknow_error_message)
        }
    }

    private fun renderCurrencySelector(currencies: List<Currency>, chosenCurrency: Currency) {
        chosenCurrencyAdapter?.addAll(currencies.toMutableList())
        val index = currencies.indexOf(chosenCurrency)
        if (index != -1)
            binding.menuSelectedCurrency.setText(chosenCurrency.currencyCode, false)
    }

    private fun renderSortInfo(sortSettings: SortSettings) {
        val textRes = when (sortSettings) {
            SortSettings.NONE -> R.string.not_sorted
            SortSettings.ALPHABET_ASC -> R.string.by_alphabet_asc
            SortSettings.ALPHABET_DESC -> R.string.by_alphabet_desc
            SortSettings.VALUE_ASC -> R.string.by_value_asc
            SortSettings.VALUE_DESC -> R.string.by_value_desc
        }

        binding.buttonSort.text = getString(textRes)
    }

    private fun renderExchangeUpdateTime(date: Date) {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val dateStr = sdf.format(date)
        binding.tvUpdateTime.text = getString(R.string.exchange_rate_form, dateStr)
    }
}