package com.example.bibliotheque
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var bookAdapter: BookAdapter
    private var books: List<Book> = emptyList()
    private lateinit var cart: MutableList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadCart()

        bookAdapter = BookAdapter(books, bookClickListener = { book ->
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra(BookDetailActivity.EXTRA_BOOK, book)
            }
            startActivity(intent)
        }, onAddToCartButtonClicked = { book ->
            val cartItem = CartItem(book, 1)
            if (cart.contains(cartItem)) {
                val index = cart.indexOf(cartItem)
                val item = cart[index]
                item.quantity++
            } else {
                cart.add(cartItem)
            }
            saveCart()
        })

        val bookRecyclerView = findViewById<RecyclerView>(R.id.bookRecyclerView)

        bookRecyclerView.adapter = bookAdapter
        bookRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        loadBooks()

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(androidx.transition.R.string.abc_search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredBooks = books.filter { it.title.contains(query!!, true) }
                bookAdapter.updateBooks(filteredBooks)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredBooks = books.filter { it.title.contains(newText!!, true) }
                bookAdapter.updateBooks(filteredBooks)
                return true
            }
        })

        val cartButton = findViewById<Button>(R.id.action_cart)
        cartButton.setOnClickListener {
            val cartIntent = Intent(this, CartActivity::class.java)
            startActivityForResult(cartIntent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loadCart()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item:MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.searchView -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        saveCart()
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

    private fun loadBooks() {
        uiScope.launch {
            val api = BookApi.create()
            api.getBooks().enqueue(object : Callback<List<Book>> {
                override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                    books = response.body()!!
                    bookAdapter.updateBooks(books)
                }

                override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                }
            })
        }
    }
}