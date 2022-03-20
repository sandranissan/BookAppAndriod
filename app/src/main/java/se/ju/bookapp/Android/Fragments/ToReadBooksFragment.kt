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
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_to_read_books.*
import se.ju.bookapp.Android.*
import se.ju.bookapp.Android.BookListModel.ListItem
import se.ju.bookapp.Android.BookListModel.ListVolumeInfo
import se.ju.bookapp.Android.R

class ToReadBooksFragment : Fragment(), BookListClickListener {

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
        val view = inflater.inflate(R.layout.fragment_to_read_books, container, false)
        val nextBtn : Button = view.findViewById(R.id.readButton)

        nextBtn.setOnClickListener {
            findNavController().navigate(R.id.readBooksFragment)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        fetchToReadBookList()
    }

    private fun fetchToReadBookList () {
        loadingProgressBar.isVisible = true
        lifecycleScope.launchWhenCreated {
            val docRef = db.collection("toReadList").document(auth!!.uid).collection("Books")
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
//                    readButton.isVisible = true
                }
                .addOnFailureListener { exception ->
                    Log.d("My TO Read Books Page", "get failed with ", exception)
                    if(loadingProgressBar != null)
                        loadingProgressBar.isVisible = false
//                    readButton.isVisible = true
                }
        }
    }

    private fun setupRecyclerView() = ToReadBooksRv.apply {
        println("SearchFragment: setUpRecyclerView")
        bookListAdapter = BookListAdapter(this@ToReadBooksFragment)
        adapter = bookListAdapter
        layoutManager = LinearLayoutManager(this@ToReadBooksFragment.context)
    }

    override fun onItemClick(listVolumeInfo: ListVolumeInfo, bookId: String) {
        val bundle = bundleOf("listVolumeInfo" to listVolumeInfo, "bookId" to bookId)
        findNavController().navigate(R.id.specificBookPageFragment, bundle)
    }
}