package com.coldfier.currencyobserver.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coldfier.currencyobserver.R
import com.coldfier.currencyobserver.data.repositories.models.ExchangePair
import com.coldfier.currencyobserver.databinding.RvExchangeRateBinding

class ExchangeRatesAdapter(
    private val onFavoriteClicked: (exchangePair: ExchangePair) -> Unit
) : ListAdapter<ExchangePair, ExchangeRatesAdapter.ExchangeRatesViewHolder>(
    ExchangeRatesDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRatesViewHolder {
        return ExchangeRatesViewHolder(
            RvExchangeRateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExchangeRatesViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ExchangeRatesViewHolder(
        private val binding: RvExchangeRateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exchangePair: ExchangePair) {
            binding.tvCurrencyName.text = exchangePair.currencyCode
            binding.tvValue.text = exchangePair.value.toString()


            val imageRes = if (exchangePair.isFavorite) R.drawable.ic_favorite_on
                else R.drawable.ic_favorite_off

            binding.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(binding.root.context, imageRes)
            )

            binding.ivFavorite.setOnClickListener { onFavoriteClicked(exchangePair) }
        }
    }

    class ExchangeRatesDiffUtil : DiffUtil.ItemCallback<ExchangePair>() {
        override fun areItemsTheSame(oldItem: ExchangePair, newItem: ExchangePair): Boolean {
            return oldItem.currencyCode == newItem.currencyCode
        }

        override fun areContentsTheSame(oldItem: ExchangePair, newItem: ExchangePair): Boolean {
            return oldItem == newItem
        }
    }
}