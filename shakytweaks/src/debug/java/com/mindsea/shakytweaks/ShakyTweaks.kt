package com.mindsea.shakytweaks

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.mindsea.shakytweaks.ui.createTweaksActivityIntent
import com.mindsea.shakytweaks.util.ShakeDetector

private const val SHARED_PREFERENCES_NAME = "shaky_tweaks"

object ShakyTweaks {

    private var isInitialized: Boolean = false
    private val moduleImpl = LibraryModuleImpl()

    fun init(context: Context) {
        if (isInitialized) {
            throw IllegalStateException("Shaky Tweaks must be initialized only once")
        }
        isInitialized = true
        moduleImpl.init(context)
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val shakeDetector = ShakeDetector()
        shakeDetector.setListener {
            context.startActivity(createTweaksActivityIntent(context))
        }

        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    internal fun module(): LibraryModule = moduleImpl

    /**
     * Provides all dependencies for ShakyTweaks
     */
    internal interface LibraryModule {
        fun tweakProvider(): TweakProvider
        fun tweakValueResolver(): TweakValueResolver
    }

    private class LibraryModuleImpl : LibraryModule {

        private val tweakProvider = TweakProvider()
        private lateinit var tweakValueResolver: TweakValueResolver

        fun init(context: Context) {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            tweakValueResolver = TweakValueResolver(tweakProvider, sharedPreferences)
        }

        override fun tweakProvider(): TweakProvider = tweakProvider

        override fun tweakValueResolver(): TweakValueResolver = tweakValueResolver
    }
}

