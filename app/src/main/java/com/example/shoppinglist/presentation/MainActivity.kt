package com.example.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRV()
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
        viewModel.shopList.observe(this, Observer {
            shopListAdapter.submitList(it)
        })
        val buttonAdd = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAdd.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    private fun setupRV() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_items)
        shopListAdapter = ShopListAdapter()
        with(rvShopList) {
            this.adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupItemLongClickListener()
        setupItemClickListener()
        setupSwipeListener(rvShopList)
    }


    private fun setupSwipeListener(rvShopList: RecyclerView?) {
        ItemTouchHelper(object :
            SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }

        }).attachToRecyclerView(rvShopList)
    }

    private fun setupItemClickListener() {
        shopListAdapter.onShopItemClick = {
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupItemLongClickListener() {
        shopListAdapter.onShopItemLongClick = {
            viewModel.changeEnableState(it)
        }
    }
}