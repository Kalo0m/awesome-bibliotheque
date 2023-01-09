package com.example.bibliotheque
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


class BookDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BOOK = "extra_book"
    }

    private lateinit var coverImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var synopsisTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        coverImageView = findViewById(R.id.coverImageView)
        titleTextView = findViewById(R.id.titleTextView)
        priceTextView = findViewById(R.id.priceTextView)
        synopsisTextView = findViewById(R.id.synopsisTextView)
        synopsisTextView.setMovementMethod(ScrollingMovementMethod())

        findViewById<Button>(R.id.back_button).setOnClickListener {
            finish()
        }

        val book = intent.getParcelableExtra<Book>(EXTRA_BOOK)
        if (book != null) updateUi(book)
    }

    private fun updateUi(book: Book) {
        Picasso.get().load(book.cover).into(coverImageView)
        titleTextView.text = book.title
        priceTextView.text = book.price.toString()
        synopsisTextView.text = book.synopsis.joinToString(" ")
    }
}