package se.ju.bookapp.Android.Fragments
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.*
import se.ju.bookapp.Android.R


import kotlinx.android.synthetic.main.fragment_sign_in_page.*

class SignInPageFragment : Fragment() {

    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var logInBtn : Button
    private lateinit var firebaseAuth: FirebaseAuth
    var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        firebaseAuth= FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()


        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_sign_in_page, container, false)
        val signUpBtn : Button = view.findViewById(R.id.btn_to_register_fromLogin)
        etEmail = view.findViewById(R.id.etEmailSignIn)
        etPassword = view.findViewById(R.id.etPasswordSignIn)
        logInBtn = view.findViewById(R.id.loginBtnSignIn)
        logInBtn.setOnClickListener {
            logIn()
        }
        signUpBtn.setOnClickListener{ view ->
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

        if(email.isBlank() || password.isBlank()) {
            Toast.makeText(requireActivity(), getString(R.string.EmailPasswordCantBeBlank),Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){ task ->
                if(task.isSuccessful) {
                    Toast.makeText(requireContext(), getString(R.string.logInMessage), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.discoverFragment)
                }
                else {
                    Toast.makeText(requireContext(), getString(R.string.logInFailMessage), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(){
                try {
                    throw it
                } catch (e: FirebaseAuthWeakPasswordException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                } catch (e: FirebaseAuthUserCollisionException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
                catch (e: FirebaseAuthInvalidUserException) {
                    Toast.makeText(requireContext(), getString(R.string.user_doesnt_exist), Toast.LENGTH_SHORT).show()
                    println(e.errorCode)
                }
            }
    }

    private fun signInFb() {
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
                findNavController().navigate(R.id.discoverFragment)
            }

            override fun onCancel() {
                Toast.makeText(requireContext(), getString(R.string.login_cancelled), Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
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
                    getString(R.string.you_logged_in_with_this_mail) + ": " + email.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

    }
}