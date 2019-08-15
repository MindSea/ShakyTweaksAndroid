package com.mindsea.shakytweaks

import android.content.SharedPreferences
import android.util.Log
import com.mindsea.shakytweaks.Tweak.*
import com.mindsea.shakytweaks.extensions.getDouble
import com.mindsea.shakytweaks.extensions.putDouble

internal class TweakProvider(private val shakyTweaks: ShakyTweaks = ShakyTweaks) {

    private val tweakMap: MutableMap<String, Tweak> = mutableMapOf()

    val tweaks: List<Tweak>
    get() = tweakMap.map { it.value }

    private val sharedPreferences: SharedPreferences by lazy {
        if (!ShakyTweaks.isInitialized) {
            throw IllegalStateException("ShakyTweaks must be initialized first")
        }
        shakyTweaks.sharedPreferences()
    }

    fun storeTweak(key: String, tweak: Tweak) {
        if (tweakMap.containsKey(key)) {
            throw IllegalStateException("Tweak key must be unique. Key $key was already added")
        }
        Log.d("ShakyTweaks", "Storing $tweak with key $key")
        tweakMap[key] = tweak
    }

    fun getValue(key: String): Any? {
        return when (val tweak = findTweak(key)) {
            is BooleanTweak -> sharedPreferences.getBoolean(key, tweak.defaultValue)
            is IntTweak -> sharedPreferences.getInt(key, tweak.defaultValue)
            is DoubleTweak -> sharedPreferences.getDouble(key, tweak.defaultValue)
            is LongTweak -> sharedPreferences.getLong(key, tweak.defaultValue)
            is FloatTweak -> sharedPreferences.getFloat(key, tweak.defaultValue)
            is StringTweak -> sharedPreferences.getString(key, tweak.defaultValue)
        }
    }

    private fun findTweak(key: String): Tweak {
        return tweakMap[key] ?: throw IllegalStateException("Could not find tweak with key $key")
    }

    inline fun <reified T> getTypedValue(key: String): T {
        return getValue(key) as T? ?: throw IllegalStateException("Tweak with id $key registered with wrong type")
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
        when (val tweak = findTweak(key)) {
            is IntTweak -> {
                val updatedValue = getTypedValue<Int>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is LongTweak -> {
                val updatedValue = getTypedValue<Long>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is DoubleTweak -> {
                val updatedValue = getTypedValue<Double>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is FloatTweak -> {
                val updatedValue = getTypedValue<Float>(key) + (tweak.step * stepMultiplier)
                if (updatedValue > tweak.minValue && updatedValue < tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            else -> throw UnsupportedOperationException("Tweak $tweak doesn't support increment")
        }
    }

    companion object {
        val instance = TweakProvider()
    }
}