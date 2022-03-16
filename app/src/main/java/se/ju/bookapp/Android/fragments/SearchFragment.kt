package se.ju.bookapp.Android.fragments

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
import se.ju.bookapp.Android.BookClickListener
import se.ju.bookapp.Android.MainActivity
import se.ju.bookapp.Android.Model.VolumeInfo
import se.ju.bookapp.Android.R
import se.ju.bookapp.Android.SearchResultAdapter
import se.ju.bookapp.Android.databinding.FragmentSearchBinding
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val TAG ="SearchFragment"

class SearchFragment : Fragment(), BookClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSearchBinding

    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
        print("${volumeInfo.title}")
        Toast.makeText(context, volumeInfo.title, Toast.LENGTH_SHORT).show()
        val bundle = bundleOf("volumeInfo" to volumeInfo, "bookId" to bookId)
        findNavController().navigate(R.id.specificBookPageFragment, bundle)
    }
}


