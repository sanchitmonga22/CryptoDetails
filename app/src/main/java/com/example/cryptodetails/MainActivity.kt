package com.example.cryptodetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cryptodetails.databinding.ActivityMainBinding
import com.example.cryptodetails.util.ContextHolder
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextHolder.app = application
        ContextHolder.context = baseContext

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_my_account,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}

// exception handling for network
// bug fix
// use kotlin flows

//        // Add your own reselected listener
//        binding.navView.setOnItemReselectedListener { item ->
//            // Pop everything up to the reselected item
//            val reselectedDestinationId = item.itemId
//            navController.popBackStack(reselectedDestinationId, inclusive = false)
//        }
