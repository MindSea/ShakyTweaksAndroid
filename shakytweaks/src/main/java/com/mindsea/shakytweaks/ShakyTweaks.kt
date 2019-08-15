package com.mindsea.shakytweaks

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorManager
import com.mindsea.shakytweaks.ui.createTweaksActivityIntent
import com.mindsea.shakytweaks.util.ShakeDetector

private const val SHARED_PREFERENCES_NAME = "shaky_tweaks"

object ShakyTweaks {

    internal var isInitialized: Boolean = false
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (isInitialized) {
            throw IllegalStateException("Shaky Tweaks must be initialized only once")
        }
        isInitialized = true
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val shakeDetector = ShakeDetector()
        shakeDetector.setListener {
            context.startActivity(createTweaksActivityIntent(context))
        }

        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    internal fun sharedPreferences(): SharedPreferences {
        return sharedPreferences ?: throw IllegalStateException("SharedPreferences must be initialized")
    }
}