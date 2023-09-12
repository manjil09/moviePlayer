package com.manjil.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.manjil.movieapp.databinding.ActivityMainBinding
import com.manjil.movieapp.feature.homePage.HomeFragment
import com.manjil.movieapp.feature.profilePage.ProfileFragment
import com.manjil.movieapp.feature.searchPage.SearchFragment

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val profileFragment = ProfileFragment()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val spannable = SpannableString(getString(R.string.movie_browser))
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(baseContext, R.color.bright_red)),
            0,
            5,
            0
        )
        binding.tvTitle.setText(spannable, TextView.BufferType.SPANNABLE)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.search -> setCurrentFragment(searchFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }
        binding.bottomNavigation.selectedItemId = R.id.home

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flContainer, fragment)
            commit()
        }
    }
}