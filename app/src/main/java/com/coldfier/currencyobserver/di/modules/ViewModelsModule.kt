package com.coldfier.currencyobserver.di.modules

import androidx.lifecycle.ViewModel
import com.coldfier.currencyobserver.di.ViewModelKey
import com.coldfier.currencyobserver.ui.MainViewModel
import com.coldfier.currencyobserver.ui.favorites.FavoritesViewModel
import com.coldfier.currencyobserver.ui.popular.PopularViewModel
import com.coldfier.currencyobserver.ui.sort.SortViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    fun bindFavoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PopularViewModel::class)
    fun bindPopularViewModel(popularViewModel: PopularViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SortViewModel::class)
    fun bindSortViewModel(sortViewModel: SortViewModel): ViewModel
}