package se.ju.bookapp.Android.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.HttpException
import se.ju.bookapp.Android.Api.RetrofitInstance
import se.ju.bookapp.Android.SearchResultClickListener
import se.ju.bookapp.Android.SearchResultModel.VolumeInfo
import se.ju.bookapp.Android.R
import se.ju.bookapp.Android.SearchResultAdapter
import se.ju.bookapp.Android.databinding.FragmentSearchBinding
import java.io.IOException

const val TAG ="SearchFragment"

class SearchFragment : Fragment(), SearchResultClickListener {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("SearchFragment: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        println("SearchFragment: onCreateView")

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        println("SearchFragment: onStart")
        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() = binding.searchResultRecyclerView.apply {
        println("SearchFragment: setUpRecyclerView")
        searchResultAdapter = SearchResultAdapter(this@SearchFragment)
        adapter = searchResultAdapter
        layoutManager = LinearLayoutManager(this@SearchFragment.context)
    }

    private fun setupSearchView() = binding.searchView.apply {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                println(query)
                if (query != null) {
                    searchBook(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchBook(query)
                }
                return true
            }

        })
    }

    private fun searchBook (query: String) {
        val urlQuery = query.replace(" ", "+")

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getBooksByQuery(urlQuery)
            }
            catch (e: IOException){
                Log.e(TAG, "IOException")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }
            catch (e: HttpException){
                Log.e(TAG, "HTTPException")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null){
                searchResultAdapter.items = response.body()!!.items
            }
            else {
                Log.e(TAG, "Response Unsuccessful")
            }
            binding.progressBar.isVisible = false
        }
    }

    override fun onItemClick(volumeInfo: VolumeInfo, bookId: String) {
        Toast.makeText(context, volumeInfo.title, Toast.LENGTH_SHORT).show()
        val bundle = bundleOf("volumeInfo" to volumeInfo, "bookId" to bookId)
        findNavController().navigate(R.id.specificBookPageFragment, bundle)
    }
}


