package com.example.shoppinglist.domain

data class ShopItem(
    val id: Int,
    val count: Int,
    val name: String,
    val enabled: Boolean,
)
