package se.ju.bookapp.Android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import se.ju.bookapp.Android.Model.Item
import se.ju.bookapp.Android.databinding.BookItemBinding

class SearchResultAdapter(private val bookClickListener: BookClickListener): RecyclerView.Adapter<SearchResultAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            val bookVolume = items[position].volumeInfo
            val bookId = items[position].id
            bookClickListener.onItemClick(bookVolume, bookId)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var items: List<Item>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(BookItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.binding.apply {
            val book = items[position]

            textViewId.text = book.id

            if (book.volumeInfo.title != null){
                textViewTitle.text = book.volumeInfo.title
            }

            if (book.volumeInfo.authors != null){
                textViewAuthor.text = book.volumeInfo.authors.joinToString(", ")
            }
            else{
                textViewAuthor.text = "Unknown Author"
            }

            if (book.volumeInfo.imageLinks != null) {
                var imageUrl = book.volumeInfo.imageLinks.thumbnail
                    .replace("http://", "https://")

                imageViewBook.load(imageUrl){
                    crossfade(true)
                    placeholder(R.drawable.ic_baseline_menu_book_24)
                    transformations(CircleCropTransformation())
                }
            }
            else{
                imageViewBook.load("https://toppng.com/uploads/preview/book-11549420966kupbnxvyyl.png"){
                    crossfade(true)
                    placeholder(R.drawable.ic_baseline_menu_book_24)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}