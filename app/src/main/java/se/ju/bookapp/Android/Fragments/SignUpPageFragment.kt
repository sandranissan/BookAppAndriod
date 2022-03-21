package se.ju.bookapp.Android.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import se.ju.bookapp.Android.R

class SignUpPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var emailEditText : EditText;
    private lateinit var nameEditText : EditText;
    private lateinit var passwordEditText : EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var registerButton : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_sign_up_page, container, false)
        nameEditText = view.findViewById(R.id.etNameSignUp)
        emailEditText = view.findViewById(R.id.etEmailSignUp)
        passwordEditText = view.findViewById(R.id.etPasswordSignUp)
        registerButton = view.findViewById(R.id.signUpBtnSignUp)

        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment

        registerButton.setOnClickListener {
            signUpUser()
        }
        return  view
    }

    private fun signUpUser() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        //validate
        if(name.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(requireActivity(), "Name, Email and password can't be blank",Toast.LENGTH_SHORT).show()
            return
        }
        //if name, email and password is okey
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity()){ task ->
            if(task.isSuccessful) {
                Log.d(TAG,"createUserWithEmail:success")
                Toast.makeText(requireContext(), "Successfully Signed up", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.discoverFragment)

            } else {
                Toast.makeText(requireContext(), "Signed up failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}