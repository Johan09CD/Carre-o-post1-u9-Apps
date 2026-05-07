package com.udes.carttdd.domain.repository

import com.udes.carttdd.domain.model.CartItem

interface CartRepository {
    suspend fun getItems(): List<CartItem>
    suspend fun addItem(item: CartItem): Boolean
    suspend fun removeItem(id: String): Boolean
}