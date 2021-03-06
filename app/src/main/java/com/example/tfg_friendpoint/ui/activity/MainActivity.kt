package com.example.tfg_friendpoint.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tfg_friendpoint.R
import com.example.tfg_friendpoint.databinding.ActivityMainBinding
import com.example.tfg_friendpoint.repository.AuthRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var loggedUser: FirebaseUser
    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        bottomNavigationView = mBinding.bottomNav
        navController = this.findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)
        /*setSupportActionBar(mBinding.topAppBar)
        setupActionBarWithNavController(navController)*/

    }
    private fun showAuthActivity() {
            val authActivityIntent = Intent(this, AuthActivity::class.java).apply {
            }
            startActivity(authActivityIntent)
    }


}