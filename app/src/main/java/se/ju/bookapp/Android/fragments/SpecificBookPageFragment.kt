package se.ju.bookapp.Android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.android.synthetic.main.fragment_specific_book_page.*
import se.ju.bookapp.Android.Model.VolumeInfo
import se.ju.bookapp.Android.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SpecificBookPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpecificBookPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_book_page, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupBookDetail()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SpecificBookPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SpecificBookPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun setupBookDetail(){
        val volumeInfo = arguments?.getParcelable<VolumeInfo>("volumeInfo")
        if(volumeInfo!!.title != null) {
            titleTv.text = volumeInfo.title
        }

        if (volumeInfo!!.authors != null){
            authorTv.text = volumeInfo.authors.joinToString(", ")
        }
        else{
            authorTv.text = "Unknown Author"
        }

        if (volumeInfo!!.description != null){
            descriptionTv.text = volumeInfo.description
        }
        else{
            descriptionTv.text = "Unknown"
        }

        if (volumeInfo!!.pageCount != null){
            pageCountTv.text = "${volumeInfo.pageCount} pages"
        }
        else{
            pageCountTv.text = "Unknown"
        }

        if (volumeInfo.imageLinks != null) {
            var imageUrl = volumeInfo.imageLinks.thumbnail
                .replace("http://", "https://")

            bookCoverIv.load(imageUrl){
                crossfade(true)
                placeholder(R.drawable.ic_baseline_menu_book_24)
                transformations(CircleCropTransformation())
            }
        }
        else{
            bookCoverIv.load("https://toppng.com/uploads/preview/book-11549420966kupbnxvyyl.png"){
                crossfade(true)
                placeholder(R.drawable.ic_baseline_menu_book_24)
                transformations(CircleCropTransformation())
            }
        }
    }
}