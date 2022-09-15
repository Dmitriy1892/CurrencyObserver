package com.coldfier.currencyobserver.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.coldfier.currencyobserver.di.modules.DataStoreModule
import com.coldfier.currencyobserver.di.modules.DatabaseModule
import com.coldfier.currencyobserver.di.modules.NetworkModule
import com.coldfier.currencyobserver.di.modules.ViewModelsModule
import com.coldfier.currencyobserver.ui.MainActivity
import com.coldfier.currencyobserver.ui.base.BaseFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelsModule::class, NetworkModule::class, DatabaseModule::class, DataStoreModule::class
    ]
)
interface AppComponent {

    fun inject(baseFragment: BaseFragment<ViewBinding, ViewModel>)
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}