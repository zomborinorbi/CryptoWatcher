package com.example.cryptowatcher.repository
import com.example.cryptowatcher.model.Crypto
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    /**
     * Fetches the [Crypto] list with the given size from the API.
     *
     * @param listSize the size of the list.
     */
    suspend fun fetchCryptoList(listSize: Int)

    /**
     * Fetches the [Crypto] with the given ID from the API.
     *
     * @param cryptoId the id of the [Crypto].
     */
    suspend fun fetchCryptoDetails(cryptoId: String)

    /**
     * Returns the stored [Crypto] list.
     *
     * @return the stored [Crypto] list, `null` in case an error or if no data has been fetched.
     */
    fun getCryptoList(): StateFlow<List<Crypto>?>

    /**
     * Returns the loading state of the crypto list.
     *
     * @return `true` if the list is loading, `false` otherwise.
     */
    fun isCryptoListLoading(): StateFlow<Boolean>

    /**
     * Returns the error message related to the crypto list.
     *
     * @return `null` if there is no error, otherwise the error message.
     */
    fun getCryptoListError(): StateFlow<String?>

    /**
     * Returns the stored [Crypto] with the given ID.
     *
     * @param cryptoId the ID of the [Crypto] to retrieve.
     * @return the [Crypto] related to the given ID.
     */
    fun getCryptoDetails(cryptoId: String): Flow<Crypto>

    /**
     * Returns the loading state of the crypto details.
     *
     * @return `true` if the details are loading, `false` otherwise.
     */
    fun isCryptoDetailsLoading(): StateFlow<Boolean>

    /**
     * Returns the error message related to the crypto details.
     *
     * @return `null` if there is no error present, otherwise the error message.
     */
    fun getCryptoDetailsError(): StateFlow<String?>
}
