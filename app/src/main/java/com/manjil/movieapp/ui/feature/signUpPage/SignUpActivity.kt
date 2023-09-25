package com.manjil.movieapp.ui.feature.signUpPage

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.manjil.movieapp.ui.MainActivity
import com.manjil.movieapp.R
import com.manjil.movieapp.databinding.ActivitySignUpBinding
import com.manjil.movieapp.util.NetworkUtil


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth
        binding.progressbar.visibility = View.INVISIBLE

        binding.btSignUp.setOnClickListener {
            binding.progressbar.visibility = View.VISIBLE
            signUpUser()
        }
    }

    private fun signUpUser() {

        val email: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            showToastMessage("Please enter your email.")
            binding.progressbar.visibility = View.INVISIBLE
            return
        }
        if (password.isEmpty()) {
            showToastMessage("Please enter your password.")
            binding.progressbar.visibility = View.INVISIBLE
            return
        }

        if (NetworkUtil.isNetworkAvailable(this)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("signUp", "createUserWithEmail:success")
                        showToastMessage("Sign up successful.")
                        binding.progressbar.visibility = View.INVISIBLE

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        binding.progressbar.visibility = View.INVISIBLE
                        val exception = task.exception
                        Log.w("signUp", "createUserWithEmail:failure", task.exception)

                        when (exception) {
                            is FirebaseAuthWeakPasswordException -> {
                                binding.etPassword.error = getString(R.string.error_weak_password)
                                binding.etPassword.requestFocus()
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                binding.etEmail.error = getString(R.string.error_invalid_email)
                                binding.etEmail.requestFocus()
                            }

                            is FirebaseAuthUserCollisionException -> {
                                binding.etEmail.error = getString(R.string.error_user_exists)
                                binding.etEmail.requestFocus()
                            }

                            else -> showToastMessage("An internal error has occurred. Please check your connection.")
                        }
                    }
                }
        } else {
            showToastMessage("No internet connection. Please check your network settings.")
            binding.progressbar.visibility = View.INVISIBLE
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }
}