package com.example.shoppinglist.domain

data class ShopItem(
    val count: Int,
    val name: String,
    val enabled: Boolean,
    var id: Int = -UNDEFINED_ID,
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
