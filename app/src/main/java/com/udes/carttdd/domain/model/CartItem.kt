package com.udes.carttdd.domain.model

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val qty: Int
)