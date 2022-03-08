package se.ju.bookapp.Android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import se.ju.bookapp.Android.fragments.DiscoverFragment
import se.ju.bookapp.Android.fragments.MyBooksFragment
import se.ju.bookapp.Android.fragments.ProfileFragment
import se.ju.bookapp.Android.fragments.SearchFragment
import android.widget.Toast
import com.facebook.AccessToken
import com.google.firebase.ktx.Firebase
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_in_page.*


class MainActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth?=null
    var callbackManager: CallbackManager?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        login_button.setReadPermissions("email")
        login_button.setOnClickListener {
            signIn()
        }



        //val discoverFragment = DiscoverFragment()
        //val myBooksFragment = MyBooksFragment()
        //val profileFragment = ProfileFragment()
        //val searchFragment = SearchFragment()

        //makeCurrentFragment(discoverFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigation.setupWithNavController(navController)





    }

    private fun signIn() {
        login_button.registerCallback(callbackManager, object:FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result!!.accessToken)
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
            .addOnFailureListener { e->
                Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener { result ->

                //get email
                val email =  result.user!!.email
                Toast.makeText(this, "du loggade in med detta email:" + email, Toast.LENGTH_LONG).show()
            }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode,resultCode, data)

    }


    // private fun makeCurrentFragment(fragment: Fragment) =
        //    supportFragmentManager.beginTransaction().apply {
         //       replace(R.id.fragment_container, fragment)
        //        commit()
        //}
}