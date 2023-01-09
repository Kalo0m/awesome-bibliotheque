package com.example.bibliotheque

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartActivity : AppCompatActivity() {

    private lateinit var cart: MutableList<CartItem>
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        loadCart()

        val cartRecyclerView = findViewById<RecyclerView>(R.id.cart_recycler_view)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(cart, this::onRemoveButtonClicked)
        cartRecyclerView.adapter = adapter

        updateTotalPrice()

        findViewById<Button>(R.id.back_button).setOnClickListener {
            saveCart()
            finish()
        }
    }

    private fun onRemoveButtonClicked(item: CartItem) {
        item.quantity--
        if (item.quantity == 0) {
            cart.remove(item)
            adapter.notifyDataSetChanged()
        }
        saveCart()
        updateTotalPrice()
        adapter.notifyDataSetChanged()
    }

    private fun updateTotalPrice() {
        var total = 0
        for (item in cart) {
            total += (item.book.price * item.quantity).toInt()
        }
        findViewById<TextView>(R.id.total_price_text_view).text = "Total : ${total}€"
    }

    override fun onPause() {
        saveCart()
        super.onPause()
    }

    override fun onStop() {
        saveCart()
        super.onStop()
    }

    private fun saveCart() {
        val sharedPreferences = getSharedPreferences("cart", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(cart)
        editor.putString("cart", json)
        editor.apply()
    }

    private fun loadCart() {
        val sharedPreferences = getSharedPreferences("cart", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("cart", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            cart = Gson().fromJson(json, type)
        } else {
            cart = mutableListOf()
        }
    }
}