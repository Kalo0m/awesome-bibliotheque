package com.example.bibliotheque

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CartAdapter(
    private val items: List<CartItem>,
    private val onRemoveButtonClicked: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: ImageView = view.findViewById(R.id.coverImageView)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
        val removeButton: Button = view.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val book = item.book

        Picasso.get()
            .load(book.cover)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.coverImageView)

        holder.titleTextView.text = book.title

        holder.quantityTextView.text = item.quantity.toString()

        holder.priceTextView.text = book.price.toString()

        holder.removeButton.setOnClickListener {
            onRemoveButtonClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}