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

import android.content.SharedPreferences
import com.mindsea.shakytweaks.extensions.getDouble
import com.mindsea.shakytweaks.extensions.putDouble

internal class TweakValueResolver(
    private val tweakProvider: TweakProvider,
    private val sharedPreferences: SharedPreferences
) {
    class TweakValueOutOfRangeException(
        val thresholdType: ThresholdType,
        val attemptedValue: String,
        val thresholdValue: String
    ) : Exception("The value $attemptedValue is ${if (thresholdType == ThresholdType.MIN) "smaller" else "larger" } than the maximum value (${thresholdValue})") {
        enum class ThresholdType {
            MIN, MAX
        }
    }

    inline fun <reified T> getTypedValue(key: String): T {
        return getValue(key) as T?
            ?: throw IllegalArgumentException("Tweak with id $key registered with wrong type")
    }

    inline fun <reified T> getTypedValueOptional(key: String): T? {
        return getValue(key) as T?
    }

    fun getValue(key: String): Any? {
        return when (val tweak = tweakProvider.findTweak(key)) {
            is Tweak.BooleanTweak -> sharedPreferences.getBoolean(key, tweak.tweakValue)
            is Tweak.IntTweak -> sharedPreferences.getInt(key, tweak.tweakValue)
            is Tweak.DoubleTweak -> sharedPreferences.getDouble(key, tweak.tweakValue)
            is Tweak.LongTweak -> sharedPreferences.getLong(key, tweak.tweakValue)
            is Tweak.FloatTweak -> sharedPreferences.getFloat(key, tweak.tweakValue)
            is Tweak.StringTweak -> sharedPreferences.getString(key, tweak.tweakValue)
            is Tweak.StringResOptionsTweak -> sharedPreferences.getInt(key, tweak.tweakValue)
            is Tweak.StringOptionsTweak -> sharedPreferences.getString(key, tweak.tweakValue)
            is Tweak.ActionTweak -> tweak.action
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
            is String? -> editor.putString(key, value)
            else -> throw IllegalStateException("update value of type not implemented")
        }
        editor.apply()
    }

    fun resetAllTweaks() {
        val edit = sharedPreferences.edit()
        tweakProvider.tweaks.map(Tweak::id)
            .forEach { key ->
                edit.remove(key)
            }
        edit.apply()
    }

    fun incrementValue(key: String) = incrementValueBy(key, 1)

    fun decrementValue(key: String) = incrementValueBy(key, -1)

    private fun incrementValueBy(key: String, stepMultiplier: Int) {
        when (val tweak = tweakProvider.findTweak(key)) {
            is Tweak.IntTweak -> {
                val updatedValue = getTypedValue<Int>(key) + (tweak.step * stepMultiplier)
                if (updatedValue >= tweak.minValue && updatedValue <= tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is Tweak.LongTweak -> {
                val updatedValue = getTypedValue<Long>(key) + (tweak.step * stepMultiplier)
                if (updatedValue >= tweak.minValue && updatedValue <= tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is Tweak.DoubleTweak -> {
                val updatedValue = getTypedValue<Double>(key) + (tweak.step * stepMultiplier)
                if (updatedValue >= tweak.minValue && updatedValue <= tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            is Tweak.FloatTweak -> {
                val updatedValue = getTypedValue<Float>(key) + (tweak.step * stepMultiplier)
                if (updatedValue >= tweak.minValue && updatedValue <= tweak.maxValue) {
                    updateValue(key, updatedValue)
                }
            }
            else -> throw UnsupportedOperationException("Tweak $tweak doesn't support increment")
        }
    }

    fun allowsDecimals(key: String): Boolean {
        return when (val tweak = tweakProvider.findTweak(key)) {
            is Tweak.IntTweak -> false
            is Tweak.LongTweak -> false
            is Tweak.FloatTweak -> true
            is Tweak.DoubleTweak -> true
            else -> throw UnsupportedOperationException("Tweak $tweak isn't a number")
        }
    }

    fun allowsNegativeNumbers(key: String): Boolean {
        return when (val tweak = tweakProvider.findTweak(key)) {
            is Tweak.IntTweak -> tweak.minValue < 0
            is Tweak.LongTweak -> tweak.minValue < 0
            is Tweak.FloatTweak -> tweak.minValue < 0
            is Tweak.DoubleTweak -> tweak.minValue < 0
            else -> throw UnsupportedOperationException("Tweak $tweak isn't a number")
        }
    }

    fun updateValueFromString(key: String, string: String) {
        when (val tweak = tweakProvider.findTweak(key)) {
            is Tweak.IntTweak -> setTweakValueFromString(key, string, {s -> s.toInt() }, tweak.minValue, tweak.maxValue)
            is Tweak.FloatTweak -> setTweakValueFromString(key, string, {s -> s.toFloat() }, tweak.minValue, tweak.maxValue)
            is Tweak.LongTweak -> setTweakValueFromString(key, string, {s -> s.toLong() }, tweak.minValue, tweak.maxValue)
            is Tweak.DoubleTweak -> setTweakValueFromString(key, string, {s -> s.toDouble() }, tweak.minValue, tweak.maxValue)
            else -> throw UnsupportedOperationException("Tweak $tweak doesn't support updateValueFromString")
        }
    }

    private fun <T: Comparable<T>> setTweakValueFromString(
        key: String,
        string: String,
        mapper: (String) -> T,
        minValue: T,
        maxValue: T
    ) {
        val updatedValue = mapper(string)
        when {
            updatedValue < minValue -> {
                throw TweakValueOutOfRangeException(
                    TweakValueOutOfRangeException.ThresholdType.MIN,
                    updatedValue.toString(),
                    minValue.toString()
                )
            }
            updatedValue > maxValue -> {
                throw TweakValueOutOfRangeException(
                    TweakValueOutOfRangeException.ThresholdType.MAX,
                    updatedValue.toString(),
                    maxValue.toString()
                )
            }
            else -> {
                updateValue(key, updatedValue)
            }
        }
    }
}