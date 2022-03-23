package se.ju.bookapp.Android.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_specific_book_page.*
import se.ju.bookapp.Android.BookListModel.ListImageLinks
import se.ju.bookapp.Android.BookListModel.ListVolumeInfo
import se.ju.bookapp.Android.SearchResultModel.VolumeInfo
import se.ju.bookapp.Android.R


class SpecificBookPageFragment : Fragment() {
    private var auth = Firebase.auth.currentUser
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specific_book_page, container, false)
    }

    override fun onStart() {
        super.onStart()

        val volumeInfo = arguments?.getParcelable<VolumeInfo>("volumeInfo")
        var listVolumeInfo = arguments?.getParcelable<ListVolumeInfo>("listVolumeInfo")
        val bookId = arguments?.getString("bookId")

        if(volumeInfo != null){
            var bookThumbnails = ListImageLinks(volumeInfo.imageLinks?.thumbnail)
            listVolumeInfo = ListVolumeInfo(
            volumeInfo.authors,
            volumeInfo.description,
            bookThumbnails,
            volumeInfo.pageCount,
            volumeInfo.title)
        }

        if (listVolumeInfo != null) {

            setupBookDetail(listVolumeInfo)
        }

        addToMyWantToReadBtn.setOnClickListener{
            if(auth != null){
                if (listVolumeInfo != null && bookId != null) {
                    checkIfBookInToReadList(listVolumeInfo, bookId)
                }
            }
            else{
                logInDialog()
            }
        }

        addToMyReadBtn.setOnClickListener{
            if(auth != null){
                if (listVolumeInfo != null && bookId != null){
                    checkIfBookInReadList(listVolumeInfo, bookId)
                }
            }
            else{
                logInDialog()
            }
        }
    }

    private fun logInDialog(){
        AlertDialog.Builder(this.context)
            .setTitle(getString(R.string.NeedToSignIn))
            .setMessage(getString(R.string.WouldSignIn))
            .setPositiveButton(
                getString(R.string.Yes)
            ) { _, _ ->
                findNavController().navigate(R.id.signInPageFragment)
            }.setNegativeButton(
                getString(R.string.No)
            ) { _, _ ->

            }.show()
    }

    private fun checkIfBookInToReadList(volumeInfo: ListVolumeInfo, bookId: String){
        val docRef = db.collection("readList").document(auth!!.uid).collection("Books").document(bookId)
        docRef.get()
            .addOnSuccessListener {
                if(it.exists()){
                    AlertDialog.Builder(this.context)
                        .setTitle(getString(R.string.BookExist))
                        .setMessage(getString(R.string.ReallyWantToAdd))
                        .setPositiveButton(
                            getString(R.string.Yes)
                        ) { _, _ ->
                            addToToReadList(volumeInfo, bookId)
                            deleteFromReadList(bookId)
                        }.setNegativeButton(
                            getString(R.string.No)
                        ) { _, _ ->

                        }.show()
                }
                else{
                    addToToReadList(volumeInfo, bookId)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Specific Book Page", "get failed with ", exception)
            }
    }

    private fun checkIfBookInReadList(listVolumeInfo: ListVolumeInfo, bookId: String){
        val docRef = db.collection("toReadList").document(auth!!.uid).collection("Books").document(bookId)
        docRef.get()
            .addOnSuccessListener {
                if(it.exists()){
                    println("BOOK IN TO-READ-LIST")
                    AlertDialog.Builder(this.context)
                        .setTitle(getString(R.string.Existingbook))
                        .setMessage(getString(R.string.AddBookTo))
                        .setPositiveButton(
                            getString(R.string.Yes)
                        ) { _, _ ->
                            addToReadList(listVolumeInfo, bookId)
                            deleteFromToReadList(bookId)
                        }.setNegativeButton(
                            getString(R.string.No)
                        ) { _, _ ->

                        }.show()
                }
                else{
                    addToReadList(listVolumeInfo, bookId)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Specific Book Page", "get failed with ", exception)
            }
    }

    private fun addToToReadList(listVolumeInfo: ListVolumeInfo, bookId: String){
        db.collection("toReadList").document(auth!!.uid).collection("Books").document(bookId).set(listVolumeInfo)
            .addOnSuccessListener {
                Log.d("SpecificBookPage", "DocumentSnapshot successfully written!")
                Toast.makeText(this.context, getString(R.string.book_added_to_to_read_list), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                    e -> Log.w("SpecificBookPage", "Error writing document", e)
                    Toast.makeText(this.context, getString(R.string.failed_to_add_to_to_read_list), Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToReadList(listVolumeInfo: ListVolumeInfo, bookId: String){
        db.collection("readList").document(auth!!.uid).collection("Books").document(bookId).set(listVolumeInfo)
            .addOnSuccessListener {
                Log.d("SpecificBookPage", "DocumentSnapshot successfully written!")
                Toast.makeText(this.context, getString(R.string.book_added_to_read_list), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                    e -> Log.w("SpecificBookPage", "Error writing document", e)
                    Toast.makeText(this.context, getString(R.string.failed_to_add_to_read_list), Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteFromReadList(bookId: String) {
        db.collection("readList").document(auth!!.uid).collection("Books").document(bookId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    private fun deleteFromToReadList(bookId: String) {
        db.collection("toReadList").document(auth!!.uid).collection("Books").document(bookId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }


    private fun setupBookDetail(listVolumeInfo: ListVolumeInfo?){

        println(listVolumeInfo)

        if(listVolumeInfo?.title.isNullOrEmpty()){
            titleTv.isVisible = false
        }
        titleTv.text = listVolumeInfo?.title

        if(listVolumeInfo?.authors.isNullOrEmpty()){
            authorTv.isVisible = false
        }
        authorTv.text = listVolumeInfo?.authors?.joinToString(", ")

        if(listVolumeInfo?.description.isNullOrEmpty()){
            descriptionTv.isVisible = false
        }
        descriptionTv.text = listVolumeInfo?.description

        if (listVolumeInfo?.pageCount == 0){
            pageCountTv.isVisible = false
        }
        pageCountTv.text = listVolumeInfo?.pageCount.toString() + " " + getString(R.string.pages)

        var imageUrl = listVolumeInfo?.imageLinks?.thumbnail
            ?.replace("http://", "https://")

        bookCoverIv.load(imageUrl){
            crossfade(true)
            placeholder(R.drawable.ic_baseline_menu_book_24)
        }
    }
}