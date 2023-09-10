package com.example.musicplayer

import android.app.Application
import android.content.Context

class ApplicationClass : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}