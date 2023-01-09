package com.example.bibliotheque

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BookAdapter(
    private var books: List<Book>,
    private val bookClickListener: (Book) -> Unit,
    private var onAddToCartButtonClicked: (Book) -> Unit
) : RecyclerView.Adapter<BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view, bookClickListener, onAddToCartButtonClicked)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}


class BookViewHolder(
    itemView: View,
    private val bookClickListener: (Book) -> Unit,
    private val onAddToCartButtonClicked: (Book) -> Unit

) : RecyclerView.ViewHolder(itemView) {

    private val coverImageView = itemView.findViewById<ImageView>(R.id.coverImageView)
    private val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
    private val priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)
    private val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)

    fun bind(book: Book) {
        Picasso.get().load(book.cover).into(coverImageView)
        titleTextView.text = book.title
        priceTextView.text = book.price.toString()
        itemView.setOnClickListener { bookClickListener(book) }
        addToCartButton.setOnClickListener { onAddToCartButtonClicked(book) }
    }
}