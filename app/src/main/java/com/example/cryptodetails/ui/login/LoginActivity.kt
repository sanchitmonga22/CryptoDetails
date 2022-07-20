package com.example.cryptodetails.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cryptodetails.databinding.ActivityLoginBinding
import com.example.cryptodetails.ui.home.MainActivity
import com.example.cryptodetails.util.ContextHolder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAppWideContext()
        checkIfLoggedInAndNavigateToMain()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setupGoogleOAuth()
    }

    private fun checkIfLoggedInAndNavigateToMain() {
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            navigateToMainActivity()
        }
    }

    private fun setAppWideContext() {
        ContextHolder.app = application
        ContextHolder.context = baseContext
    }

    private fun setupGoogleOAuth() {
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        binding.googleSignIn?.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        googleSignInActivityResult.launch(gsc.signInIntent)
    }

    private val googleSignInActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { googleSignInAccount ->
            GoogleSignIn.getSignedInAccountFromIntent(googleSignInAccount.data)
                .addOnCompleteListener {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login was successful: ${it.isSuccessful}",
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnSuccessListener {
                    Log.d("sign in successful", it.displayName.toString())
                    navigateToMainActivity()
                }.addOnCanceledListener {
                    Toast.makeText(this@LoginActivity, "Login was CANCELLED", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@LoginActivity, "Login FAILED", Toast.LENGTH_LONG).show()
                }
        }

    private fun navigateToMainActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}