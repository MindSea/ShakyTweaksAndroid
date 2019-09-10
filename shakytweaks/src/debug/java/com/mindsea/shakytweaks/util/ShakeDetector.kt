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

package com.mindsea.shakytweaks.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.lang.Math.sqrt

private const val SHAKE_THRESHOLD_GRAVITY = 2.7f
private val SHAKE_SLOP_TIME_MS = 500

/**
 * approach described on https://jasonmcreynolds.com/?p=388
 */
internal class ShakeDetector : SensorEventListener {

    private var onShake: (() -> Unit)? = null
    private var shakeTimestamp: Long = 0

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (onShake == null) {
            return
        }

        // gForce will be close to 1 when there is no movement.
        val gForce = event.gForce()

        if (gForce < SHAKE_THRESHOLD_GRAVITY) {
            return
        }

        val now = System.currentTimeMillis()
        // ignore shake events too close to each other (500ms)
        if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
            return
        }

        shakeTimestamp = now

        onShake?.invoke()
    }

    fun setListener(listener: () -> Unit) {
        this.onShake = listener
    }

    private fun SensorEvent.gForce(): Double {
        val x = this.values[0]
        val y = this.values[1]
        val z = this.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        return sqrt((gX * gX + gY * gY + gZ * gZ).toDouble())
    }
}