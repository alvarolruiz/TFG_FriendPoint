package com.example.tfg_friendpoint.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.ActivityMainBinding
import com.example.tfg_friendpoint.ui.fragments.ChatFragment
import com.example.tfg_friendpoint.ui.fragments.FriendPointFragment
import com.example.tfg_friendpoint.ui.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        bottomNavigationView = findViewById(R.id.bottomNav)
        navController = this.findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)
        setSupportActionBar(mBinding.topAppBar)
        setupActionBarWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    /*private fun setupBottomNav() {
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
        mBinding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.action_home -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment).show(homeFragment).commit()
                    mActiveFragment = homeFragment
                    true
                }
                R.id.action_fp_center -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(friendPointFragment).commit()
                    mActiveFragment = friendPointFragment
                    true
                }
                R.id.action_chats -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(chatFragment).commit()
                    mActiveFragment = chatFragment
                    true
                }
                else -> false
            }


        }


    }*/
}