package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem


class ShopListMapper {

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enabled = shopItemDbModel.enabled,
        id = shopItemDbModel.id
    )

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
        name = shopItem.name,
        id = shopItem.id,
        count = shopItem.count,
        enabled = shopItem.enabled
    )

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}