import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bibliotheque.Book
import com.example.bibliotheque.R
import com.squareup.picasso.Picasso

class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView: TextView = itemView.findViewById<TextView>(R.id.titleTextView)
    private val coverImageView: ImageView = itemView.findViewById<ImageView>(R.id.coverImageView)

    fun bind(book: Book) {
        titleTextView.text = book.title
        Picasso.get()
            .load(book.cover)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(coverImageView)
    }
}