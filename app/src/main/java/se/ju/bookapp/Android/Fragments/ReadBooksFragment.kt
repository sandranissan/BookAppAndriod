package se.ju.bookapp.Android.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_read_books.*
import kotlinx.android.synthetic.main.fragment_to_read_books.*
import kotlinx.android.synthetic.main.fragment_to_read_books.loadingProgressBar
import se.ju.bookapp.Android.BookListAdapter
import se.ju.bookapp.Android.BookListClickListener
import se.ju.bookapp.Android.BookListModel.ListItem
import se.ju.bookapp.Android.BookListModel.ListVolumeInfo
import se.ju.bookapp.Android.R

class ReadBooksFragment : Fragment(), BookListClickListener {

    private lateinit var bookListAdapter: BookListAdapter

    private lateinit var bookList: ArrayList<ListItem>

    private lateinit var bookItem: ListItem

    private var auth = Firebase.auth.currentUser
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_read_books, container, false)
        val prevBtn : Button = view.findViewById(R.id.toReadButton)

        prevBtn.setOnClickListener {
            findNavController().navigate(R.id.toReadBooksFragment)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        fetchReadBookList()
    }

    private fun fetchReadBookList () {
        loadingProgressBar.isVisible = true
        lifecycleScope.launchWhenCreated {
            loadingProgressBar.isVisible = true
            val docRef = db.collection("readList").document(auth!!.uid).collection("Books")
            docRef.get()
                .addOnSuccessListener { documents ->
                    bookList = ArrayList()
                    for (document in documents) {
                        bookItem = ListItem(document.id, document.toObject(ListVolumeInfo::class.java))
                        bookList.add(bookItem)
                    }
                    bookListAdapter.items = bookList
                    if(loadingProgressBar != null)
                        loadingProgressBar.isVisible = false
//                    toReadButton.isVisible = true
                }
                .addOnFailureListener { exception ->
                    Log.d("My TO Read Books Page", "get failed with ", exception)
                    if(loadingProgressBar != null)
                        loadingProgressBar.isVisible = false
//                    toReadButton.isVisible = true
                }
        }
    }

    private fun setupRecyclerView() = ReadBooksRv.apply {
        println("SearchFragment: setUpRecyclerView")
        bookListAdapter = BookListAdapter(this@ReadBooksFragment)
        adapter = bookListAdapter
        layoutManager = LinearLayoutManager(this@ReadBooksFragment.context)
    }

    override fun onItemClick(listVolumeInfo: ListVolumeInfo, bookId: String) {
        val bundle = bundleOf("listVolumeInfo" to listVolumeInfo, "bookId" to bookId)
        findNavController().navigate(R.id.specificBookPageFragment, bundle)
    }

}