package com.example.eyeOnTheNews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EyeOnTheNewsApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }

}