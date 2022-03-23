package se.ju.bookapp.Android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import se.ju.bookapp.Android.SearchResultModel.ImageLinks
import se.ju.bookapp.Android.SearchResultModel.Item
import se.ju.bookapp.Android.SearchResultModel.VolumeInfo
import se.ju.bookapp.Android.databinding.BookItemBinding

class SearchResultAdapter(private val searchResultClickListener: SearchResultClickListener): RecyclerView.Adapter<SearchResultAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            val bookVolume = items[position].volumeInfo
            val bookId = items[position].id
            searchResultClickListener.onItemClick(bookVolume, bookId)
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

            var volumeInfo = checkIfVolumeInfoNull(book.volumeInfo, this.bookCardView.context)

            if (volumeInfo.title.isNullOrEmpty())
                textViewTitle.isVisible = false
            else
                textViewTitle.text = book.volumeInfo.title

            if (volumeInfo.authors.isNullOrEmpty())
                textViewAuthor.isVisible = false
            else
                textViewAuthor.text = book.volumeInfo.authors.joinToString(", ")

            imageViewBook.load(book.volumeInfo.imageLinks!!.thumbnail){
                crossfade(true)
                placeholder(R.drawable.ic_baseline_menu_book_24)
                transformations(CircleCropTransformation())
            }
        }
    }

    private fun checkIfVolumeInfoNull(volumeInfo: VolumeInfo, context: Context): VolumeInfo {
        if(volumeInfo.authors.isNullOrEmpty())
            volumeInfo.authors = listOf()
        if(volumeInfo.title.isNullOrEmpty())
            volumeInfo.title = ""
        if(volumeInfo.imageLinks == null){
            var imageLink: ImageLinks = ImageLinks(context.getString(R.string.DefaultBookCover))
            volumeInfo.imageLinks = imageLink
        }
        else{
            volumeInfo.imageLinks!!.thumbnail = volumeInfo.imageLinks!!.thumbnail
                .replace("http://", "https://")
        }
        if(volumeInfo.description.isNullOrEmpty()){
            volumeInfo.description = ""
        }
        return volumeInfo
    }
}