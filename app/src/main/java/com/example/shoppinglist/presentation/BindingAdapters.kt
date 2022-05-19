package com.example.shoppinglist.presentation

import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("tilCountError")
fun bindCountError(textView: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        "Вводите только цифры"
    } else {
        null
    }
    textView.error = message
}

@BindingAdapter("tilNameError")
fun bindNameError(textView: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        "Вводите только буквы"
    } else {
        null
    }
    textView.error = message
}
