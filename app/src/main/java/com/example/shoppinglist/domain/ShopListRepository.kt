package com.example.shoppinglist.domain

interface ShopListRepository {

    fun getShopList(): List<ShopItem>

    fun getShopItemById(id: Int): ShopItem

    fun editShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun addShopItem(item: ShopItem)
}