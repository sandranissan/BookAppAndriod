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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.facebook.AccessToken

import com.google.firebase.ktx.Firebase
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import kotlinx.android.synthetic.main.fragment_sign_in_page.*
import se.ju.bookapp.Android.fragments.*


class MainActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null
    var callbackManager: CallbackManager? = null
    var mAuthListener : FirebaseAuth.AuthStateListener? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragmentContainerView)
        //val signOutBtn : Button = findViewById(R.id.signOutBtn)

        bottomNavigation.setupWithNavController(navController)


        bottomNavigation.setOnItemSelectedListener(){
            val isLoggedIn = Firebase.auth.currentUser
            println("isLoggedI,  $isLoggedIn" )
            println("User email, ${isLoggedIn?.email}")
            navController.navigate(it.itemId)
            when(it.itemId){
                R.id.myBooksFragment -> {
                    if (isLoggedIn !=null){
                        navController.navigate(R.id.myBooksFragment)
                    } else {
                        Toast.makeText(this, "You need to Log in to see you're books", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.signInPageFragment)
                    }
                }
                R.id.profileFragment -> {
                    if (isLoggedIn !=null){
                        navController.navigate(R.id.profileFragment)

                    } else {
                        Toast.makeText(this, "You need to Log in", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.signInPageFragment)
                    }

                }
            }
            true
        }

    }

}


// private fun makeCurrentFragment(fragment: Fragment) =
//    supportFragmentManager.beginTransaction().apply {
//       replace(R.id.fragment_container, fragment)
//        commit()
//}
