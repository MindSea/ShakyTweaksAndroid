package com.mindsea.shakytweaks

import android.content.SharedPreferences
import com.mindsea.shakytweaks.extensions.getDouble
import com.mindsea.shakytweaks.extensions.putDouble

internal class TweakValueResolver(
    private val tweakProvider: TweakProvider,
    private val sharedPreferences: SharedPreferences
) {

    inline fun <reified T> getTypedValue(key: String): T {
        return getValue(key) as T? ?: throw IllegalArgumentException("Tweak with id $key registered with wrong type")
    }

    fun getValue(key: String): Any? {
        return when (val tweak = tweakProvider.findTweak(key)) {
            is Tweak.BooleanTweak -> sharedPreferences.getBoolean(key, tweak.defaultValue)
            is Tweak.IntTweak -> sharedPreferences.getInt(key, tweak.defaultValue)
            is Tweak.DoubleTweak -> sharedPreferences.getDouble(key, tweak.defaultValue)
            is Tweak.LongTweak -> sharedPreferences.getLong(key, tweak.defaultValue)
            is Tweak.FloatTweak -> sharedPreferences.getFloat(key, tweak.defaultValue)
            is Tweak.StringTweak -> sharedPreferences.getString(key, tweak.defaultValue)
        }
    }

    fun <T> updateValue(key: String, value: T) {
        val editor = sharedPreferences.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Double -> editor.putDouble(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is String -> editor.putString(key, value)
            else -> throw IllegalStateException("update value of type not implemented")
        }
        editor.apply()
    }

    fun incrementValue(key: String) = incrementValueBy(key, 1)


    fun decrementValue(key: String) = incrementValueBy(key, -1)

    private fun incrementValueBy(key: String, stepMultiplier: Int) {
        when (val tweak = tweakProvider.findTweak(key)) {
            is Tweak.IntTweak -> {
                val updatedValue = getTypedValue<Int>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is Tweak.LongTweak -> {
                val updatedValue = getTypedValue<Long>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is Tweak.DoubleTweak -> {
                val updatedValue = getTypedValue<Double>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is Tweak.FloatTweak -> {
                val updatedValue = getTypedValue<Float>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            else -> throw UnsupportedOperationException("Tweak $tweak doesn't support increment")
        }
    }
}