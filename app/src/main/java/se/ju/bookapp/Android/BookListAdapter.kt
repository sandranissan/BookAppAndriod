package se.ju.bookapp.Android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import se.ju.bookapp.Android.BookListModel.ListItem
import se.ju.bookapp.Android.databinding.BookItemBinding

class BookListAdapter(private val bookListClickListener: BookListClickListener): RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            val bookVolume = items[position].volumeInfo
            val bookId = items[position].id
            bookListClickListener.onItemClick(bookVolume, bookId)
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            val bookVolume = items[position].volumeInfo
            val bookId = items[position].id
            bookListClickListener.onItemLongClick(bookVolume, bookId)
            return true
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ListItem>(){
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var items: List<ListItem>
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

            if (book.volumeInfo.title.isNullOrEmpty())
                textViewTitle.isVisible = false
            else
                textViewTitle.text = book.volumeInfo.title

            if (book.volumeInfo.authors.isNullOrEmpty())
                textViewAuthor.isVisible = false
            else
                textViewAuthor.text = book.volumeInfo.authors!!.joinToString(", ")

            imageViewBook.load(book.volumeInfo.imageLinks!!.thumbnail){
                crossfade(true)
                placeholder(R.drawable.ic_baseline_menu_book_24)
                transformations(CircleCropTransformation())
            }
        }
    }
}