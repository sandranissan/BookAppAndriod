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

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val discoverFragment = DiscoverFragment()
        //val myBooksFragment = MyBooksFragment()
        //val profileFragment = ProfileFragment()
        //val searchFragment = SearchFragment()

        //makeCurrentFragment(discoverFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigation.setupWithNavController(navController)

        FacebookSdk.sdkInitialize(getApplicationContext());



    }

   // private fun makeCurrentFragment(fragment: Fragment) =
        //    supportFragmentManager.beginTransaction().apply {
         //       replace(R.id.fragment_container, fragment)
        //        commit()
        //}
}