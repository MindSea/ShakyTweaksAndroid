/*
 * MIT License
 *
 * Copyright (c) 2019 MindSea
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

