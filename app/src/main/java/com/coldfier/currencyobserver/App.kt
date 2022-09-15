package com.coldfier.currencyobserver

import android.app.Application
import com.coldfier.currencyobserver.di.AppComponent
import com.coldfier.currencyobserver.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}