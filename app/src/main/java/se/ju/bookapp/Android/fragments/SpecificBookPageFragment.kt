package se.ju.bookapp.Android.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_specific_book_page.*
import se.ju.bookapp.Android.Model.VolumeInfo
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
        val bookId = arguments?.getString("bookId")

        if (volumeInfo != null) {
            setupBookDetail(volumeInfo)
        }

        addToMyWantToReadBtn.setOnClickListener{
            if (volumeInfo != null && bookId != null) {
                checkIfBookInToReadList(volumeInfo, bookId)
            }
        }

        addToMyReadBtn.setOnClickListener{
            if (volumeInfo != null && bookId != null){
                checkIfBookInReadList(volumeInfo, bookId)
            }
        }
    }

    private fun checkIfBookInToReadList(volumeInfo: VolumeInfo, bookId: String){
        val docRef = db.collection("readList").document(auth!!.uid).collection("Books").document(bookId)
        docRef.get()
            .addOnSuccessListener {
                if(it.exists()){
                    println("BOOK IN READ-LIST")
                    AlertDialog.Builder(this.context)
                        .setTitle("Book exists in: Read List")
                        .setMessage("Do you really want to add it to: To Read List")
                        .setPositiveButton(
                            "Yes"
                        ) { _, _ ->
                            addToToReadList(volumeInfo, bookId)
                            deleteFromReadList(bookId)
                        }.setNegativeButton(
                            "No"
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

    private fun checkIfBookInReadList(volumeInfo: VolumeInfo, bookId: String){
        val docRef = db.collection("toReadList").document(auth!!.uid).collection("Books").document(bookId)
        docRef.get()
            .addOnSuccessListener {
                if(it.exists()){
                    println("BOOK IN TO-READ-LIST")
                    AlertDialog.Builder(this.context)
                        .setTitle("Book exists in: To Read List")
                        .setMessage("Do you really want to add it to: Read List")
                        .setPositiveButton(
                            "Yes"
                        ) { _, _ ->
                            addToReadList(volumeInfo, bookId)
                            deleteFromToReadList(bookId)
                        }.setNegativeButton(
                            "No"
                        ) { _, _ ->

                        }.show()
                }
                else{
                    addToReadList(volumeInfo, bookId)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Specific Book Page", "get failed with ", exception)
            }
    }

    private fun addToToReadList(volumeInfo: VolumeInfo, bookId: String){
        db.collection("toReadList").document(auth!!.uid).collection("Books").document(bookId).set(volumeInfo)
            .addOnSuccessListener { Log.d("SpecificBookPage", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("SpecificBookPage", "Error writing document", e) }
    }

    private fun addToReadList(volumeInfo: VolumeInfo, bookId: String){
        db.collection("readList").document(auth!!.uid).collection("Books").document(bookId).set(volumeInfo)
            .addOnSuccessListener { Log.d("SpecificBookPage", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("SpecificBookPage", "Error writing document", e) }
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


    private fun setupBookDetail(volumeInfo: VolumeInfo){
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
            }
        }
        else{
            bookCoverIv.load("https://toppng.com/uploads/preview/book-11549420966kupbnxvyyl.png"){
                crossfade(true)
                placeholder(R.drawable.ic_baseline_menu_book_24)
            }
        }
    }
}