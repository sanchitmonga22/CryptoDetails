package com.example.cryptodetails.ui.home

import android.Manifest
import android.content.Intent
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
import com.example.cryptodetails.R
import com.example.cryptodetails.app.CustomBroadcastReceiver
import com.example.cryptodetails.app.NetworkStatusChangeReceiver
import com.example.cryptodetails.databinding.ActivityMainBinding
import com.example.cryptodetails.ui.login.LoginActivity
import com.example.cryptodetails.ui.map.MapsFragment
import com.example.cryptodetails.ui.myAccount.MyAccountFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val customBroadcastReceiver: CustomBroadcastReceiver = CustomBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerBroadcastReceivers()
        retrieveRegistrationToken()

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.flavorCategory.text =
            "${getString(R.string.flavor_category)} ${com.example.cryptodetails.BuildConfig.FIELD_NAME_CATEGORY} ${com.example.cryptodetails.BuildConfig.FIELD_NAME_SDK}"
        setContentView(binding.root)

        setupNavView()
    }

    private fun setupNavView() {
        val navView: BottomNavigationView = binding.navView

        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_my_account,
                R.id.navigation_notifications,
                R.id.map,
                R.id.media_player
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun retrieveRegistrationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(
                    baseContext,
                    "Fetching FCM registration token failed",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnCompleteListener
            }
            val token = task.result
            Toast.makeText(baseContext, token.toString(), Toast.LENGTH_SHORT).show()
        })
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
//        unregisterReceiver(NetworkStatusChangeReceiver())
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
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == MyAccountFragment.WRITE_EXTERNAL_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == MapsFragment.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        unregisterBroadcastReceivers()
        super.onDestroy()
    }

    fun signOut() {
        GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        ).signOut().addOnCompleteListener {
            Toast.makeText(
                this,
                "Logout was successful: ${it.isSuccessful}",
                Toast.LENGTH_LONG
            ).show()
        }.addOnSuccessListener {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Logout FAILED",
                Toast.LENGTH_LONG
            ).show()
        }.addOnCanceledListener {
            Toast.makeText(
                this,
                "Logout CANCELLED",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}