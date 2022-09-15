package com.coldfier.currencyobserver.di.modules

import com.coldfier.currencyobserver.BuildConfig
import com.coldfier.currencyobserver.data.sources.net.TimestampToDateAdapter
import com.coldfier.currencyobserver.data.sources.net.api.CurrenciesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideCurrenciesApi(): CurrenciesApi = provideRetrofit().create(CurrenciesApi::class.java)

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .add(TimestampToDateAdapter())
                        .build()
                )
            )
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                val logger = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                if (BuildConfig.DEBUG) addInterceptor(logger)
            }
            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader("apikey", BuildConfig.API_KEY)
                    .build()
                chain.proceed(request)
            }
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}