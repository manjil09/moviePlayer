package com.manjil.movieapp.ui.feature.loginPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.manjil.movieapp.ui.MainActivity
import com.manjil.movieapp.ui.feature.signUpPage.SignUpActivity
import com.manjil.movieapp.databinding.ActivityLoginBinding
import com.manjil.movieapp.util.NetworkUtil

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth
        binding.progressBar.visibility = View.INVISIBLE


        if (firebaseAuth.currentUser != null) {
            startMainActivity()
        }

        binding.btLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            Log.d("login", "onCreate: ${NetworkUtil.isNetworkAvailable(this)}")
            loginUser()
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            showToastMessage("Please enter your email.")
            binding.progressBar.visibility = View.INVISIBLE
            return
        }
        if (password.isEmpty()) {
            showToastMessage("Please enter your password.")
            binding.progressBar.visibility = View.INVISIBLE
            return
        }

        if (NetworkUtil.isNetworkAvailable(this)) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        startMainActivity()
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        val exception = task.exception
                        Log.w("login", "signInWithEmail:failure", task.exception)

                        if (exception is FirebaseAuthInvalidCredentialsException || exception is FirebaseAuthInvalidUserException) {
                            binding.etPassword.error =
                                "The password is invalid or the user does not exist."
                            binding.etPassword.requestFocus()
                        } else
                            showToastMessage("An internal error has occurred. Please check your connection.")
                    }
                }
        } else {
            showToastMessage("No internet connection. Please check your network settings.")
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}