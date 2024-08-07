package com.example.cryptowatcher.viewmodels

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.cryptowatcher.repository.CryptoRepository
import com.example.cryptowatcher.testdata.TEST_CRYPTO
import com.example.cryptowatcher.testdata.TEST_CRYPTO_ID
import com.example.cryptowatcher.ui.viewmodels.CryptoDetailsScreenViewModel
import com.example.cryptowatcher.ui.viewmodels.CryptoDetailsScreenViewModel.Companion.CRYPTO_ID_SAVED_STATE_KEY
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CryptoDetailsScreenViewModelTest {

    private val mockCryptoRepository: CryptoRepository = mockk(relaxed = true)

    private lateinit var mockSavedStateHandle: SavedStateHandle

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockSavedStateHandle = SavedStateHandle(
            mapOf(CRYPTO_ID_SAVED_STATE_KEY to TEST_CRYPTO_ID)
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCryptoDetails is called with the correct cryptoId on creation`() = runTest {
        // Given
        createViewModel()

        advanceUntilIdle()

        // Then
        coVerify { mockCryptoRepository.fetchCryptoDetails(TEST_CRYPTO_ID) }
    }

    @Test
    fun `uiState returns the correct state with the details and isDataRefreshing true when its loading`() = runTest {
        // Given
        every { mockCryptoRepository.isCryptoDetailsLoading() } returns MutableStateFlow(true)
        every { mockCryptoRepository.getCryptoDetails(TEST_CRYPTO_ID) } returns MutableStateFlow(TEST_CRYPTO)

        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            // initial item
            assertNull(awaitItem())

            assertEquals(CryptoDetailsScreenViewModel.UiState(TEST_CRYPTO, true), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState returns the correct state with the details and isDataRefreshing false when its not loading`() = runTest {
        // Given
        every { mockCryptoRepository.isCryptoDetailsLoading() } returns MutableStateFlow(false)
        every { mockCryptoRepository.getCryptoDetails(TEST_CRYPTO_ID) } returns MutableStateFlow(TEST_CRYPTO)

        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            // initial item
            assertNull(awaitItem())

            assertEquals(CryptoDetailsScreenViewModel.UiState(TEST_CRYPTO, false), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    private fun createViewModel() =
        CryptoDetailsScreenViewModel(mockSavedStateHandle, mockCryptoRepository)
}
