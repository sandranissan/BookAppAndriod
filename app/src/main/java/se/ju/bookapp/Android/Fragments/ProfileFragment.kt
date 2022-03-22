package se.ju.bookapp.Android.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.facebook.login.LoginManager
import se.ju.bookapp.Android.R
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View? {
        auth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val signOutBtn : Button = view.findViewById(R.id.signOutBtn)

        val userEmailTv = view.findViewById<TextView>(R.id.userEmailTV)

        userEmailTv.text = auth.currentUser?.email

        signOutBtn.setOnClickListener {
            LoginManager.getInstance().logOut()
            auth.signOut()
            view.findNavController().navigate(R.id.discoverFragment)
        }
        return view
    }
}