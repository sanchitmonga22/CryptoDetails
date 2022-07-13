package com.example.cryptodetails

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cryptodetails.app.CustomBroadcastReceiver
import com.example.cryptodetails.app.NetworkStatusChangeReceiver
import com.example.cryptodetails.databinding.ActivityMainBinding
import com.example.cryptodetails.ui.myAccount.MyAccountFragment
import com.example.cryptodetails.util.ContextHolder
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val customBroadcastReceiver: CustomBroadcastReceiver = CustomBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextHolder.app = application
        ContextHolder.context = baseContext

        registerBroadcastReceivers()

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

    private fun registerBroadcastReceivers() {
        registerReceiver(
            NetworkStatusChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        /**
         * [registerReceiver]
         * @Override
         * public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter,
         * @Nullable String broadcastPermission, @Nullable Handler scheduler) {
         *
         * provides options to add permissions that needs to be requested, before someone can send a broadcast to this app.
         *
         * CUSTOM_PERMISSION: A receiver can create a custom permission that can be required by the broadcaster, in order to broadcast a message
         * the permission can be defined in the [Manifest.permission] in the <permission> tag in the reciever and that also needs to be updated in
         * the <user-permission> tag in the broadcast app
         *
         * [LocalBroadcastManager]:
         * similar to livedata, can be used within the app
         */
        registerReceiver(customBroadcastReceiver, IntentFilter(CustomBroadcastReceiver.ACTION))
    }

    private fun unregisterBroadcastReceivers() {
        unregisterReceiver(NetworkStatusChangeReceiver())
        unregisterReceiver(customBroadcastReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MyAccountFragment.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == MyAccountFragment.WRITE_EXTERNAL_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcastReceivers()
    }
}

// bug fix for navigation between tabs
//
// use kotlin flows

//        // Add your own reselected listener
//        binding.navView.setOnItemReselectedListener { item ->
//            // Pop everything up to the reselected item
//            val reselectedDestinationId = item.itemId
//            navController.popBackStack(reselectedDestinationId, inclusive = false)
//        }
