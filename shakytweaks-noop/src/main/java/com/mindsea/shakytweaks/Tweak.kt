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

import androidx.annotation.StringRes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class TweakDefaultValueDelegate<T>(private val releaseValue: T) : ReadOnlyProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = releaseValue
}

@Suppress("UNUSED_PARAMETER")
fun booleanTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Boolean, tweakValue: Boolean? = null): ReadOnlyProperty<Any, Boolean> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun intTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Int, minValue: Int, maxValue: Int, tweakValue: Int? = null, step: Int = 0): ReadOnlyProperty<Any, Int> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun floatTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Float, minValue: Float, maxValue: Float, tweakValue: Float? = null, step: Float = 0F): ReadOnlyProperty<Any, Float> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun doubleTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Double, minValue: Double, maxValue: Double, tweakValue: Double? = null, step: Double): ReadOnlyProperty<Any, Double> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun longTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Long, minValue: Long, maxValue: Long, tweakValue: Long? = null, step: Long): ReadOnlyProperty<Any, Long> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun stringTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: String? = null, tweakValue: String? = null): ReadOnlyProperty<Any, String?> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun stringOptionsTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: String, vararg otherOptions: String): ReadOnlyProperty<Any, String> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun stringResOptionsTweak(tweakId: String, group: String, tweakDescription: String, @StringRes releaseValue: Int, @StringRes vararg otherOptions: Int): ReadOnlyProperty<Any, Int> =
    TweakDefaultValueDelegate(releaseValue)

@Suppress("UNUSED_PARAMETER")
fun registerActionTweak(tweakId: String, group: String, tweakDescription: String, tweakAction: () -> Unit) {
    // Do nothing
}

@Suppress("UNUSED_PARAMETER")
fun unregisterActionTweak(tweakId: String) {
    // Do nothing
}