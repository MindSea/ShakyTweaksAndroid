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

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.view.KeyEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mindsea.shakytweaks.ui.createTweaksActivityIntent
import com.mindsea.shakytweaks.util.ShakeDetector
import java.util.*

private const val SHARED_PREFERENCES_NAME = "shaky_tweaks"

object ShakyTweaks: LifecycleEventObserver {

    private val moduleImpl = LibraryModuleImpl()

    private val shakeDetector = ShakeDetector()

    private var sKeyPressMS = 0L
    private var tKeyPressMS = 0L

    private const val KEY_PRESS_THRESHOLD = 50L

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var lifecycle: Lifecycle

    @Deprecated("Issues related to the lifecycle.\n", replaceWith = ReplaceWith("init(activity: Activity, lifecycle: Lifecycle)"))
    fun init(context: Context) {
        moduleImpl.init(context)
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val shakeDetector = ShakeDetector()
        shakeDetector.setListener {
            context.startActivity(createTweaksActivityIntent(context))
        }

        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun init(activity: Activity, lifecycle: Lifecycle) {
        this.lifecycle = lifecycle

        sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        moduleImpl.init(activity)

        shakeDetector.setListener {
            activity.startActivity(createTweaksActivityIntent(activity))
        }

        this.lifecycle.addObserver(this)
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

    /**
     * A helper function for showing the Shaky Tweaks menu when simultaneously pressing the "S" + "T" keys.
     *
     * To be able to show the Shaky Tweaks menu using the designated input shortcut you must forward `onKeyDown` events
     * from your activities into this method. Ideally to enable global access, this listener should be hooked up in a
     * base activity used throughout your app.
     *
     * **Example usage:**
     * ```
     * class BaseActivity : AppCompatActivity() {
     *     override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
     *         ShakyTweaks.onKeyDown(this, keyCode)
     *         return super.onKeyDown(keyCode, event)
     *     }
     * }
     * ```
     */
    fun onKeyDown(context: Context, keyCode: Int) {
        if (keyCode == KeyEvent.KEYCODE_S) {
            if (isKeyPressTimeInThreshold(tKeyPressMS)) {
                context.startActivity(createTweaksActivityIntent(context))
            }
            sKeyPressMS = Date().time
        }

        if (keyCode == KeyEvent.KEYCODE_T) {
            if (isKeyPressTimeInThreshold(sKeyPressMS)) {
                context.startActivity(createTweaksActivityIntent(context))
            }
            tKeyPressMS = Date().time
        }

    }

    private fun isKeyPressTimeInThreshold(pastKeyPressMS: Long): Boolean {
        return (Date().time - pastKeyPressMS) <= KEY_PRESS_THRESHOLD
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (::sensorManager.isInitialized) {
                    sensorManager.registerListener(
                        shakeDetector,
                        accelerometer,
                        SensorManager.SENSOR_DELAY_UI
                    )
                }
            }
            Lifecycle.Event.ON_STOP -> {
                if (::sensorManager.isInitialized) {
                    sensorManager.unregisterListener(shakeDetector)
                }
            }
            Lifecycle.Event.ON_DESTROY -> {
                if (::lifecycle.isInitialized) {
                    lifecycle.removeObserver(this)
                }
            }
            else -> {
            }
        }
    }
}

