package com.mindsea

import android.app.Application
import com.mindsea.shakytweaks.ShakyTweaks

class DemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ShakyTweaks.init(applicationContext)
    }
}