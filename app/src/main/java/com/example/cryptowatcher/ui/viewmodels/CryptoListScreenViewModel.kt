package com.example.cryptowatcher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowatcher.model.Crypto
import com.example.cryptowatcher.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListScreenViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository
) : ViewModel() {

    val uiState = combine(
        cryptoRepository.isCryptoListLoading(),
        cryptoRepository.getCryptoList(),
        cryptoRepository.getCryptoListError()
    ) { isLoading, cryptoList, errorMessage ->
        when {
            isLoading -> UiState.Loading
            errorMessage != null -> UiState.Error(errorMessage)
            else -> UiState.Content(cryptoList)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        initDataFetching()
    }

    private fun initDataFetching() {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                cryptoRepository.fetchCryptoList(LIST_SIZE)
                delay(MINUTE_FETCH_DELAY)
            }
        }
    }

    sealed interface UiState {

        data class Error(val errorMessage: String) : UiState

        data object Loading : UiState

        data class Content(
            val data: List<Crypto>?
        ) : UiState
    }

    companion object {
        private const val MINUTE_FETCH_DELAY = 60000L

        private const val LIST_SIZE = 10
    }
}
