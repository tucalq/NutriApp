package com.arthurlunardi.nutriapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arthurlunardi.nutriapp.ui.fragments.GuideFragment
import com.arthurlunardi.nutriapp.ui.fragments.auth.LoginFragment
import com.arthurlunardi.nutriapp.ui.fragments.auth.RegisterFragment

class AuthPageAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val COUNT = 2
    var p = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                LoginFragment()

            }
            1 -> RegisterFragment()
            2 -> GuideFragment()
            else -> LoginFragment()
        }
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Login"
            1 -> "Cadastro"
            else -> {
                "Login"
            }
        }
    }
}
