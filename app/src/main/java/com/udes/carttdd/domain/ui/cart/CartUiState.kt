package com.udes.carttdd.ui.cart

import com.udes.carttdd.domain.model.CartItem

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val items: List<CartItem>, val total: Double) : CartUiState()
    data class Error(val message: String) : CartUiState()
}