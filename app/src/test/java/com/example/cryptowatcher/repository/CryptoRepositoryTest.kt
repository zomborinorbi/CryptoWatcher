package com.example.cryptowatcher.repository

import app.cash.turbine.test
import com.example.cryptowatcher.model.ApiDetailsResponse
import com.example.cryptowatcher.model.ApiListResponse
import com.example.cryptowatcher.service.ApiService
import com.example.cryptowatcher.testdata.LIST_SIZE
import com.example.cryptowatcher.testdata.TEST_CRYPTO
import com.example.cryptowatcher.testdata.TEST_CRYPTO_ID
import com.example.cryptowatcher.testdata.TEST_CRYPTO_LIST
import com.example.cryptowatcher.testdata.TEST_ERROR_MESSAGE
import io.mockk.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test
import retrofit2.Response
import org.junit.jupiter.api.Assertions.assertTrue
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach

class CryptoRepositoryTest {

    private val apiService: ApiService = mockk()

    private lateinit var repository: CryptoRepository

    @BeforeEach
    fun setUp() {
        repository = CryptoRepositoryImpl(apiService)

        coEvery { apiService.getList() } returns mockListResponse
        coEvery { apiService.getDetails(any()) } returns mockDetailsResponse
    }

    @Test
    fun `when fetchCryptoList is called then the loading state is set correctly`() = runTest {
        // Then
        repository.isCryptoListLoading().test {
            // Initial item
            assertFalse(awaitItem())

            // When
            repository.fetchCryptoList(LIST_SIZE)
            assertTrue(awaitItem())
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when fetchCryptoList is called then the cryptoList is stored correctly`() = runTest {
        // When
        repository.fetchCryptoList(LIST_SIZE)

        // Then
        assertEquals(TEST_CRYPTO_LIST, repository.getCryptoList().first())
    }

    @Test
    fun `when fetchCryptoList is called and there were no errors the error message is null`() = runTest {
        // When
        repository.fetchCryptoList(LIST_SIZE)

        // Then
        assertNull(repository.getCryptoListError().first())
    }

    @Test
    fun `when fetchCryptoList is called and there is an error it stores the error message`() = runTest {
        // Given
        coEvery { apiService.getList() } throws RuntimeException(TEST_ERROR_MESSAGE)

        // When
        repository.fetchCryptoList(LIST_SIZE)

        // Then
        assertEquals(TEST_ERROR_MESSAGE, repository.getCryptoListError().first())
    }

    @Test
    fun `when fetchCryptoDetails is called then the loading state is set correctly`() = runTest {
        // Then
        repository.isCryptoDetailsLoading().test {
            // Initial item
            assertFalse(awaitItem())

            // When
            repository.fetchCryptoDetails(TEST_CRYPTO_ID)
            assertTrue(awaitItem())
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when fetchCryptoDetails is called then the cryptoDetails is stored correctly`() = runTest {
        // When
        repository.fetchCryptoList(LIST_SIZE)
        repository.fetchCryptoDetails(TEST_CRYPTO_ID)

        // Then
        assertEquals(TEST_CRYPTO, repository.getCryptoDetails(TEST_CRYPTO_ID).first())
    }

    @Test
    fun `when fetchCryptoDetails is called and there were no errors the error message is null`() = runTest {
        // When
        repository.fetchCryptoDetails(TEST_CRYPTO_ID)

        // Then
        assertNull(repository.getCryptoDetailsError().first())
    }

    @Test
    fun `when fetchCryptoDetails is called and there is an error it stores the error message`() = runTest {
        // Given
        coEvery { apiService.getDetails(any()) } throws RuntimeException(TEST_ERROR_MESSAGE)

        // When
        repository.fetchCryptoDetails(TEST_CRYPTO_ID)

        // Then
        assertEquals(TEST_ERROR_MESSAGE, repository.getCryptoDetailsError().first())
    }

    companion object {
        private val mockListResponse = Response.success(ApiListResponse(TEST_CRYPTO_LIST))
        private val mockDetailsResponse = Response.success(ApiDetailsResponse(TEST_CRYPTO))
    }
}