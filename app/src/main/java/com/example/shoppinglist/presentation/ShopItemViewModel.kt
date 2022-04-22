package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*
import java.lang.Exception
import java.util.*

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemByIdUseCase = GetShopItemByIdUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _finished = MutableLiveData<Unit>()
    val finished: LiveData<Unit>
        get() = _finished

    fun addShopItem(inputName: String?, inputCount: String?) {
        val count = parseCount(inputCount)
        val name = parseName(inputName)
        val validData = validData(name, count)
        if (validData) {
            val item = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(item)
            finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val count = parseCount(inputCount)
        val name = parseName(inputName)
        val validData = validData(name, count)
        if (validData) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }
    }

    fun getItemById(id: Int) {
        val item = getShopItemByIdUseCase.getShopItemById(id)
        _shopItem.value = item
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return try {
            count?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validData(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _finished.value = Unit
    }
}