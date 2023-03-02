package com.github.emilehreich.bootcamp

import android.app.Application
import android.content.Intent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application()  {


    override fun onCreate() {
        super.onCreate()

        // Launching the BoredActivity at startup
        val intent = Intent(this, ApiActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}