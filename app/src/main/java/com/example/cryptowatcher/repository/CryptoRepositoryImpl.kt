package com.example.cryptowatcher.repository

import com.example.cryptowatcher.model.Crypto
import com.example.cryptowatcher.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoRepositoryImpl @Inject constructor(private val apiService: ApiService) : CryptoRepository {

    private val _cryptoList = MutableStateFlow<List<Crypto>?>(null)
    private val _isListLoading = MutableStateFlow(false)
    private val _listError = MutableStateFlow<String?>(null)

    private val _details = MutableStateFlow<Crypto?>(null)
    private val _isDetailsLoading = MutableStateFlow(false)
    private val _detailsError = MutableStateFlow<String?>(null)

    override suspend fun fetchCryptoList(listSize: Int) {
        withContext(Dispatchers.IO) {
            try {
                _isListLoading.value = true
                _listError.value = null
                val response = apiService.getList()
                if (response.isSuccessful) {
                    _cryptoList.value = response.body()?.data?.take(listSize)
                } else {
                    _listError.value = response.message()
                }
            } catch (e: Exception) {
                _listError.value = e.message
            } finally {
                _isListLoading.value = false
            }
        }
    }

    override suspend fun fetchCryptoDetails(cryptoId: String) {
        withContext(Dispatchers.IO) {
            try {
                _isDetailsLoading.value = true
                _detailsError.value = null
                val response = apiService.getDetails(cryptoId)
                if (response.isSuccessful) {
                    _details.value = response.body()?.data
                } else {
                    _detailsError.value = response.message()
                }
            } catch (e: Exception) {
                _detailsError.value = e.message
            } finally {
                _isDetailsLoading.value = false
            }
        }
    }

    override fun getCryptoList() = _cryptoList.asStateFlow()

    override fun isCryptoListLoading() = _isListLoading.asStateFlow()

    override fun getCryptoListError() = _listError.asStateFlow()

    override fun getCryptoDetails(cryptoId: String) = combine(
        _cryptoList.filterNotNull(),
        _details
    ) { list, details ->
        details ?: list.first { it.id == cryptoId }
    }

    override fun isCryptoDetailsLoading() = _isDetailsLoading.asStateFlow()

    override fun getCryptoDetailsError() = _detailsError.asStateFlow()
}
