package com.manjil.movieapp.feature.signUpPage

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
import com.manjil.movieapp.MainActivity
import com.manjil.movieapp.R
import com.manjil.movieapp.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth

        binding.btSignUp.setOnClickListener{signUpUser()}
    }

    private fun signUpUser() {
        binding.progressbar.visibility = View.VISIBLE

        val email: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            showToastMessage("Please enter your email.")
            binding.progressbar.visibility = View.GONE
            return
        }
        if (password.isEmpty()){
            showToastMessage("Please enter your password.")
            binding.progressbar.visibility = View.GONE
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("signUp", "createUserWithEmail:success")
                    showToastMessage("Sign up successful.")
                    binding.progressbar.visibility = View.GONE

                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Log.w("signUp", "createUserWithEmail:failure", task.exception)
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        binding.etPassword.error = getString(R.string.error_weak_password)
                        binding.etPassword.requestFocus()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        binding.etEmail.error = getString(R.string.error_invalid_email)
                        binding.etEmail.requestFocus()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        binding.etEmail.error = getString(R.string.error_user_exists)
                        binding.etEmail.requestFocus()
                    } catch (e: Exception) {
                        Log.e("signUp", e.message!!)
                    }
                    showToastMessage("Authentication failed")
                    binding.progressbar.visibility = View.GONE
                }
            }
    }

    private fun showToastMessage(message: String){
        Toast.makeText(baseContext,message,Toast.LENGTH_LONG).show()
    }
}