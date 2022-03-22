package se.ju.bookapp.Android.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_to_read_books.*
import kotlinx.android.synthetic.main.fragment_to_read_books.loadingProgressBar
import retrofit2.HttpException
import se.ju.bookapp.Android.Api.RetrofitInstance
import se.ju.bookapp.Android.R
import se.ju.bookapp.Android.SearchResultAdapter
import se.ju.bookapp.Android.SearchResultClickListener
import se.ju.bookapp.Android.SearchResultModel.VolumeInfo
import java.io.IOException

class DiscoverFragment : Fragment(), SearchResultClickListener {

    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        searchBook()
    }

    private fun searchBook () =
        lifecycleScope.launchWhenCreated {
            loadingProgressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getPopularBooks()
            }
            catch (e: IOException){
                Log.e(TAG, "IOException")
                if(loadingProgressBar != null)
                    loadingProgressBar.isVisible = false
                return@launchWhenCreated
            }
            catch (e: HttpException){
                Log.e(TAG, "HTTPException")
                if(loadingProgressBar != null)
                    loadingProgressBar.isVisible = false
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null){
                searchResultAdapter.items = response.body()!!.items
            }
            else {
                Log.e(TAG, "Response Unsuccessful")
            }
            if(loadingProgressBar != null)
                loadingProgressBar.isVisible = false
        }

    private fun setupRecyclerView() = RecomendedBooksRv.apply {
        println("SearchFragment: setUpRecyclerView")
        searchResultAdapter = SearchResultAdapter(this@DiscoverFragment)
        adapter = searchResultAdapter
        layoutManager = LinearLayoutManager(this@DiscoverFragment.context)
    }

    override fun onItemClick(volumeInfo: VolumeInfo, bookId: String) {
        val bundle = bundleOf("volumeInfo" to volumeInfo, "bookId" to bookId)
        findNavController().navigate(R.id.specificBookPageFragment, bundle)
    }
}