package com.example.cryptowatcher.viewmodels

import app.cash.turbine.test
import com.example.cryptowatcher.model.Crypto
import com.example.cryptowatcher.repository.CryptoRepository
import com.example.cryptowatcher.testdata.LIST_SIZE
import com.example.cryptowatcher.testdata.TEST_CRYPTO_LIST
import com.example.cryptowatcher.testdata.TEST_ERROR_MESSAGE
import com.example.cryptowatcher.ui.viewmodels.CryptoListScreenViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CryptoListScreenViewModelTest {

    private val mockCryptoRepository: CryptoRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `uiState returns Loading state when the crypto list is loading`() = runTest {
        // Given
        every { mockCryptoRepository.isCryptoListLoading() } returns MutableStateFlow(true)
        every { mockCryptoRepository.getCryptoList() } returns MutableStateFlow<List<Crypto>?>(null)
        every { mockCryptoRepository.getCryptoListError() } returns MutableStateFlow(null)

        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            // initial item
            assertNull(awaitItem())

            assertEquals(CryptoListScreenViewModel.UiState.Loading, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState returns Error state when the crypto list is loading`() = runTest {
        // Given
        every { mockCryptoRepository.isCryptoListLoading() } returns MutableStateFlow(false)
        every { mockCryptoRepository.getCryptoList() } returns MutableStateFlow<List<Crypto>?>(null)
        every { mockCryptoRepository.getCryptoListError() } returns MutableStateFlow(TEST_ERROR_MESSAGE)

        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            // initial item
            assertNull(awaitItem())

            assertEquals(CryptoListScreenViewModel.UiState.Error(TEST_ERROR_MESSAGE), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState returns Content state when the crypto list is present`() = runTest {
        // Given
        every { mockCryptoRepository.isCryptoListLoading() } returns MutableStateFlow(false)
        every { mockCryptoRepository.getCryptoList() } returns MutableStateFlow(TEST_CRYPTO_LIST)
        every { mockCryptoRepository.getCryptoListError() } returns MutableStateFlow(null)

        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            // initial item
            assertNull(awaitItem())

            assertEquals(CryptoListScreenViewModel.UiState.Content(TEST_CRYPTO_LIST), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchCryptoList is called upon view model creation`() = runTest {
        // Given
        createViewModel()

        // Then
        coVerify { mockCryptoRepository.fetchCryptoList(LIST_SIZE) }
    }

    private fun createViewModel() = CryptoListScreenViewModel(mockCryptoRepository)
}
