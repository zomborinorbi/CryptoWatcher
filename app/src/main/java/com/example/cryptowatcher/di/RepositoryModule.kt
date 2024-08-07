package com.example.cryptowatcher.di

import com.example.cryptowatcher.repository.CryptoRepository
import com.example.cryptowatcher.repository.CryptoRepositoryImpl
import com.example.cryptowatcher.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCryptoRepository(apiService: ApiService): CryptoRepository {
        return CryptoRepositoryImpl(apiService)
    }
}
