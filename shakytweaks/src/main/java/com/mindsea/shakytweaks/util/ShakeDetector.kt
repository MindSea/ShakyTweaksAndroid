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