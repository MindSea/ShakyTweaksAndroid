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
import androidx.annotation.StringRes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class TweakDefaultValueDelegate<T>(private val defaultValue: T) : ReadOnlyProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T = defaultValue
}

@Suppress("UNUSED_PARAMETER")
fun booleanTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Boolean): ReadOnlyProperty<Any, Boolean> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun intTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Int, minValue: Int, maxValue: Int, step: Int = 0): ReadOnlyProperty<Any, Int> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun floatTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Float, minValue: Float, maxValue: Float, step: Float = 0F): ReadOnlyProperty<Any, Float> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun doubleTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Double, minValue: Double, maxValue: Double, step: Double): ReadOnlyProperty<Any, Double> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun longTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Long, minValue: Long, maxValue: Long, step: Long): ReadOnlyProperty<Any, Long> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun stringTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: String): ReadOnlyProperty<Any, String> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun stringOptionsTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: String, vararg otherOptions: String): ReadOnlyProperty<Any, String> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun stringResOptionsTweak(tweakId: String, group: String, tweakDescription: String, @StringRes defaultValue: Int, @StringRes vararg otherOptions: Int): ReadOnlyProperty<Any, Int> =
    TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun actionTweak(tweakId: String, group: String, tweakDescription: String, action: (Context) -> Unit): ReadOnlyProperty<Any, (Context) -> Unit> =
    TweakDefaultValueDelegate { _: Context -> }
