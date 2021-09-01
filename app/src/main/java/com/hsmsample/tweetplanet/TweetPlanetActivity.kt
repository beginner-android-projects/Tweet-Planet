package com.hsmsample.tweetplanet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.hsmsample.tweetplanet.databinding.ActivityTweetPlanetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetPlanetActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityTweetPlanetBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupNavigation()
    }

    //region Setting up activity startup
    private fun setupBinding() {
        dataBinding = ActivityTweetPlanetBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

//        NavigationUI.setupWithNavController(dataBinding.toolbar, navController)
    }
    //endregion

}