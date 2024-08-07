package com.example.cryptowatcher.di

import com.example.cryptowatcher.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val baseUrl = "https://api.coincap.io/v2/"

    @Provides
    @Singleton
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
