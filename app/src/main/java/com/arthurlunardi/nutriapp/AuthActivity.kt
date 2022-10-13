package com.arthurlunardi.nutriapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.arthurlunardi.nutriapp.adapters.AuthPageAdapter
import com.google.android.material.tabs.TabLayout

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setupViewPager()




    }


    private fun setupViewPager() {
        val authViewPager = findViewById<ViewPager>(R.id.authViewPager)
        authViewPager?.adapter = AuthPageAdapter(supportFragmentManager)
        val tabLayout = findViewById<TabLayout>(R.id.authTabLayout)
        tabLayout?.setupWithViewPager(authViewPager)
    }
}