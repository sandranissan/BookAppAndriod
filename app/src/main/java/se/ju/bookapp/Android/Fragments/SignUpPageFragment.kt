package se.ju.bookapp.Android.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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
        val signInBtn : Button = view.findViewById(R.id.btn_to_login_fromRegister)
        emailEditText = view.findViewById(R.id.etEmailSignUp)
        passwordEditText = view.findViewById(R.id.etPasswordSignUp)
        registerButton = view.findViewById(R.id.signUpBtnSignUp)


        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment

        registerButton.setOnClickListener {
            signUpUser()
        }
        signInBtn.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.signInPageFragment)
        }
        return  view
    }

    private fun signUpUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        //validate
        if(email.isBlank() || password.isBlank()) {
            Toast.makeText(requireActivity(), getString(R.string.EmailPasswordCantBeBlank),Toast.LENGTH_SHORT).show()
            return
        }
        //if name, email and password is okay
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity()){ task ->
            if(task.isSuccessful) {
                Log.d(TAG,"createUserWithEmail:success")
                Toast.makeText(requireContext(), getString(R.string.signUpSuccess), Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.discoverFragment)
            }
        }.addOnFailureListener(){
                try {
                    throw it
                } catch (e: FirebaseAuthWeakPasswordException) {
                    Toast.makeText(requireContext(), getString(R.string.PassWord_error) , Toast.LENGTH_SHORT).show()

                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    println(e.message)
                    Toast.makeText(requireContext(),getString(R.string.Email_bad) , Toast.LENGTH_SHORT).show()

                } catch (e: FirebaseAuthUserCollisionException) {
                    Toast.makeText(requireContext(), getString(R.string.User_exists) , Toast.LENGTH_SHORT).show()
                }
        }
    }
}