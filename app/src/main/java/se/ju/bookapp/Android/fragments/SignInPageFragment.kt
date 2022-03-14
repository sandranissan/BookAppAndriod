package se.ju.bookapp.Android.fragments
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.facebook.AccessToken
import com.google.firebase.ktx.Firebase
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import se.ju.bookapp.Android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.view.*


import kotlinx.android.synthetic.main.fragment_sign_in_page.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInPageFragment : Fragment() {

    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var logInBtn : Button
    private lateinit var firebaseAuth: FirebaseAuth
    var callbackManager: CallbackManager? = null

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
        firebaseAuth= FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()


        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_sign_in_page, container, false)
        val signup_btn : Button = view.findViewById(R.id.btn_to_register_fromLogin)
        etEmail = view.findViewById(R.id.etEmailSignIn)
        etPassword = view.findViewById(R.id.etPasswordSignIn)
        logInBtn = view.findViewById(R.id.loginBtnSignIn)
        logInBtn.setOnClickListener {
            logIn()
        }
        signup_btn.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.signUpPageFragment)
        }

        val signInBtn : LoginButton = view.findViewById(R.id.login_button)
        signInBtn.setReadPermissions("email")
        signInBtn.setOnClickListener {
            signInFb()
        }

        return view
    }

    private fun logIn() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity()){ task ->
            if(task.isSuccessful) {
                Toast.makeText(requireContext(), "Successfully logged in", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.discoverFragment)
            } else {
                Toast.makeText(requireContext(), "Log in failed", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun signInFb() {
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        // get credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnFailureListener { e ->
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener { result ->

                //get email
                val email = result.user!!.email
                Toast.makeText(
                    requireActivity(),
                    "du loggade in med detta email:" + email,
                    Toast.LENGTH_LONG
                ).show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignInPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}