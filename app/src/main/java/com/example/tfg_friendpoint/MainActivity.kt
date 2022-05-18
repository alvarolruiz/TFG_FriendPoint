package com.example.tfg_friendpoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupBottomNav()
    }

    private fun setupBottomNav(){
        mFragmentManager = supportFragmentManager
        val homeFragment = HomeFragment()
        val friendPointFragment = FriendPointFragment()
        val chatFragment = ChatFragment()
        mActiveFragment = ChatFragment()
        mFragmentManager.beginTransaction()
            .add(R.id.mFrameLayout, homeFragment, HomeFragment::class.java.name)
            .hide(homeFragment).commit()
        mFragmentManager.beginTransaction()
            .add(R.id.mFrameLayout, friendPointFragment, FriendPointFragment::class.java.name)
            .hide(friendPointFragment).commit()
        mFragmentManager.beginTransaction()
            .add(R.id.mFrameLayout, chatFragment, ChatFragment::class.java.name).commit()

       
    }
}