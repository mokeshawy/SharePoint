package com.example.sharepoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharepoint.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        // operation mange fragment
        val navHostFragment : NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController   : NavController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.navigation).setupWithNavController(navController)

        // show fragment name in the top bar
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.logInFragment, R.id.createAccountFragment , R.id.homeFragment , R.id.shareLocationFragment , R.id.profileFragment , R.id.settingFragment , R.id.mapsFragment , R.id.resetPasswordFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)

        // select fragment you need show the bottom navigation
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when(destination.id){

                R.id.homeFragment           -> binding.navigation.visibility = View.VISIBLE
                R.id.shareLocationFragment  -> binding.navigation.visibility = View.VISIBLE
                R.id.profileFragment        -> binding.navigation.visibility = View.VISIBLE
                R.id.settingFragment        -> binding.navigation.visibility = View.VISIBLE

                else -> binding.navigation.visibility = View.INVISIBLE
            }

        }
    }
}