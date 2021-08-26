package com.hsmsample.tweetplanet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TweetPlanetApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initTimberLogger()
    }

    private fun initTimberLogger() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}