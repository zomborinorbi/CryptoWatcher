package com.example.cryptowatcher.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowatcher.model.Crypto
import com.example.cryptowatcher.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cryptoRepository: CryptoRepository
) : ViewModel() {

    private val cryptoId: String = savedStateHandle[CRYPTO_ID_SAVED_STATE_KEY]!!

    val uiState = combine(
        cryptoRepository.isCryptoDetailsLoading(),
        cryptoRepository.getCryptoDetails(cryptoId)
    ) { isLoading, cryptoDetails ->
        UiState(cryptoDetails, isLoading)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            cryptoRepository.fetchCryptoDetails(cryptoId)
        }
    }

    data class UiState(
        val details: Crypto,
        val isDataRefreshing: Boolean
    )

    companion object {
        const val CRYPTO_ID_SAVED_STATE_KEY = "cryptoId"
    }
}
