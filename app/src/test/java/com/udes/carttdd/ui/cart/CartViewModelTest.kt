package com.udes.carttdd.ui.cart

import app.cash.turbine.test
import com.udes.carttdd.domain.model.CartItem
import com.udes.carttdd.domain.repository.AnalyticsService
import com.udes.carttdd.domain.repository.CartRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var repository: CartRepository
    private lateinit var analyticsService: AnalyticsService
    private lateinit var viewModel: CartViewModel

    @BeforeEach
    fun setUp() {
        repository = mockk()
        analyticsService = mockk()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = CartViewModel(repository, analyticsService)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCart emits Success state with items and total`() = runTest {
        val items = listOf(
            CartItem("1", "Libro", 25.0, 2),
            CartItem("2", "Pen", 5.0, 1)
        )
        coEvery { repository.getItems() } returns items

        viewModel.loadCart()

        val state = viewModel.uiState.value as CartUiState.Success
        assertEquals(2, state.items.size)
        assertEquals(55.0, state.total, 0.001)
    }

    @Test
    fun `loadCart emits Error when repository throws`() = runTest {
        coEvery { repository.getItems() } throws IOException("sin red")

        viewModel.loadCart()

        assertTrue(viewModel.uiState.value is CartUiState.Error)
    }

    @Test
    fun `loadCart emits Loading before Success`() = runTest {
        coEvery { repository.getItems() } returns emptyList()

        viewModel.uiState.test {
            viewModel.loadCart()
            assertEquals(CartUiState.Loading, awaitItem())
            assertTrue(awaitItem() is CartUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `calculateTotal returns 0 for empty list`() {
        assertEquals(0.0, viewModel.calculateTotal(emptyList()), 0.001)
    }
}