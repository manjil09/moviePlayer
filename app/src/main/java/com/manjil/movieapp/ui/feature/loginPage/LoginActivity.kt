package com.manjil.movieapp.ui.feature.loginPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.manjil.movieapp.ui.MainActivity
import com.manjil.movieapp.ui.feature.signUpPage.SignUpActivity
import com.manjil.movieapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth

        if (firebaseAuth.currentUser != null){
            startMainActivity()
        }

        binding.btLogin.setOnClickListener{
            loginUser()
            binding.progressBar.visibility = View.GONE

        }
        binding.tvSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(){
        binding.progressBar.visibility = View.VISIBLE

        val email: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            showToastMessage("Please enter your email.")
            return
        }
        if (password.isEmpty()){
            showToastMessage("Please enter your password.")
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("login", "signInWithEmail:success")
                    binding.progressBar.visibility = View.GONE

                    startMainActivity()
                } else {

                    Log.w("login", "signInWithEmail:failure", task.exception)
                    try {
                        throw task.exception!!
                    }catch (e: FirebaseAuthInvalidCredentialsException){
                        binding.etPassword.error = "The password is invalid or the user does not exist."
                        binding.etPassword.requestFocus()
                    }catch (e: FirebaseAuthInvalidUserException){
                        binding.etPassword.error = "The password is invalid or the user does not exist."
                        binding.etPassword.requestFocus()
                    }
                    binding.progressBar.visibility = View.GONE
                }
            }
    }

    private fun showToastMessage(message: String){
        Toast.makeText(baseContext,message,Toast.LENGTH_LONG).show()
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}