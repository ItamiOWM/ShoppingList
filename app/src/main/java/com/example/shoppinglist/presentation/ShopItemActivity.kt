package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var til_name: TextInputLayout
    private lateinit var til_count: TextInputLayout
    private lateinit var et_name: TextInputEditText
    private lateinit var et_count: TextInputEditText
    private lateinit var save_button: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(ShopItemViewModel::class.java)
        initViews()
        setEditTextListeners()
        setMode()
        observerViewModel()
    }



    private fun observerViewModel() {
        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                "Вводите только цифры"
            } else {
                null
            }
            til_count.error = message
        }
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                "Вводите латинские символы"
            } else {
                null
            }
            til_name.error = message
        }
        viewModel.finished.observe(this) {
            finish()
        }
    }

    private fun setMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getItemById(shopItemId)
        viewModel.shopItem.observe(this, Observer {
            et_count.setText(it.count.toString())
            et_name.setText(it.name)
        })
        save_button.setOnClickListener {
            viewModel.editShopItem(et_name.text.toString(), et_count.text.toString())
        }
    }

    private fun launchAddMode() {
        save_button.setOnClickListener {
            viewModel.addShopItem(et_name.text.toString(), et_count.text.toString())
        }

    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param ShopItemId is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews() {
        til_name = findViewById(R.id.til_name)
        til_count = findViewById(R.id.til_count)
        et_name = findViewById(R.id.et_name)
        et_count = findViewById(R.id.et_count)
        save_button = findViewById(R.id.save_button)
    }

    private fun setEditTextListeners() {
        et_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                til_name.error = null
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        et_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                til_count.error = null
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }


    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, id: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, id)
            return intent
        }
    }
}