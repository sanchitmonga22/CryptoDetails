package com.example.cryptodetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cryptodetails.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController
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


/**
 * {"code":"200000",
 * "data":[
 * {    "currency":"CSP",
 *      "name":"CSP",
 *      "fullName":"Caspian",
 *      "precision":8,
 *      "confirms":12,
 *      "contractAddress":"0xa6446d655a0c34bc4f05042ee88170d056cbaf45",
 *      "withdrawalMinSize":"10000",
 *      "withdrawalMinFee":"5000",
 *      "isWithdrawEnabled":true,
 *      "isDepositEnabled":true,
 *      "isMarginEnabled":false,
 *      "isDebitEnabled":false
 * },
 * {"currency":"LOKI","name":"OXEN","fullName":"Oxen","precision":8,"confirms":10,"contractAddress":"","withdrawalMinSize":"0.000000","withdrawalMinFee":"2.000000","isWithdrawEnabled":true,"isDepositEnabled":true,"isMarginEnabled":false,"isDebitEnabled":true},
 * {"currency":"NRG","name":"NRG","fullName":"Energi","precision":8,"confirms":5000,"contractAddress":"","withdrawalMinSize":"1","withdrawalMinFee":"1","isWithdrawEnabled":true,"isDepositEnabled":true,"isMarginEnabled":false,"isDebitEnabled":true},
 * {"currency":"FET","name":"FET","fullName":"Fetch.Ai","precision":8,"confirms":12,"contractAddress":"0x1d287cc25dad7ccaf76a26bc660c5f7c8e2a05bd","withdrawalMinSize":"100.000000","withdrawalMinFee":"46.000000","isWithdrawEnabled":true,"isDepositEnabled":true,"isMarginEnabled":false,"isDebitEnabled":false},
 * {"currency":"XMR","name":"XMR","fullName":"Monero","precision":8,"confirms":12,"contractAddress":"","withdrawalMinSize":"0.020000","withdrawalMinFee":"0.001000","isWithdrawEnabled":true,"isDepositEnabled":true,"isMarginEnabled":true,"isDebitEnabled":true},
 * {"currency":"RBTC","name":"RBTC","fullName":"RSK Smart Bitcoin","precision":8,"confirms":250,"contractAddress":"","withdrawalMinSize":"0.000200","withdrawalMinFee":"0.000150","isWithdrawEnabled":true,"isDepositEnabled":true,"isMarginEnabled":false,"isDebitEnabled":true},
 * {"currency":"RIF","name":"RIF","fullName":"RIF Token","precision":8,"confirms":250,"contractAddress":"","withdrawalMinSize":"20.000000","withdrawalMinFee":"10.000000","isWithdrawEnabled":true,"isDepositEnabled":true,"isMarginEnabled":false,"isDebitEnabled":true},
 */