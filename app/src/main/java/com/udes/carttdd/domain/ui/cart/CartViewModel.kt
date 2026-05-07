package com.udes.carttdd.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udes.carttdd.domain.model.CartItem
import com.udes.carttdd.domain.repository.AnalyticsService
import com.udes.carttdd.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository,
    private val analytics: AnalyticsService
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            try {
                val items = repository.getItems()
                val total = calculateTotal(items)
                _uiState.value = CartUiState.Success(items, total)
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: "Error")
            }
        }
    }

    internal fun calculateTotal(items: List<CartItem>) =
        items.sumOf { it.price * it.qty }
}