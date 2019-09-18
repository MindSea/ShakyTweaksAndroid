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

internal sealed class Tweak {

    abstract val id: String

    abstract val group: String

    abstract val description: String

    data class BooleanTweak(
        override val id: String,
        val tweakValue: Boolean,
        override val group: String,
        override val description: String) : Tweak()

    data class IntTweak(
        override val id: String,
        val tweakValue: Int,
        val minValue: Int,
        val maxValue: Int,
        val step: Int,
        override val group: String,
        override val description: String) : Tweak()

    data class DoubleTweak(
        override val id: String,
        val tweakValue: Double,
        val minValue: Double,
        val maxValue: Double,
        val step: Double,
        override val group: String,
        override val description: String) : Tweak()

    data class LongTweak(
        override val id: String,
        val tweakValue: Long,
        val minValue: Long,
        val maxValue: Long,
        val step: Long,
        override val group: String,
        override val description: String) : Tweak()

    data class FloatTweak(
        override val id: String,
        val tweakValue: Float,
        val minValue: Float,
        val maxValue: Float,
        val step: Float,
        override val group: String,
        override val description: String) : Tweak()

    data class StringTweak(
        override val id: String,
        val tweakValue: String?,
        override val group: String,
        override val description: String) : Tweak()

    data class StringOptionsTweak(
        override val id: String,
        val tweakValue: String,
        val options: Set<String>,
        override val group: String,
        override val description: String
    ) : Tweak()

    data class StringResOptionsTweak(
        override val id: String,
        @StringRes val tweakValue: Int,
        val options: Set<Int>,
        override val group: String,
        override val description: String
    ) : Tweak()

    data class ActionTweak(
        override val id: String,
        val action: (Context) -> Unit,
        override val group: String,
        override val description: String) : Tweak()

}

private val tweakProvider = ShakyTweaks.module().tweakProvider()
private val tweakValueResolver: TweakValueResolver by lazy {
    ShakyTweaks.module().tweakValueResolver()
}

private class BooleanTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean = tweakValueResolver.getTypedValue(tweakId)
}

private class IntTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int = tweakValueResolver.getTypedValue(tweakId)
}

private class FloatTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Float> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Float = tweakValueResolver.getTypedValue(tweakId)
}

private class DoubleTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Double> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Double = tweakValueResolver.getTypedValue(tweakId)
}

private class LongTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Long> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Long = tweakValueResolver.getTypedValue(tweakId)
}

private class StringTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? = tweakValueResolver.getTypedValueOptional(tweakId)
}

private class StringOptionsTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String = tweakValueResolver.getTypedValue(tweakId)
}

private class StringResOptionsTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int = tweakValueResolver.getTypedValue(tweakId)
}

fun booleanTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Boolean, tweakValue: Boolean? = null): ReadOnlyProperty<Any, Boolean> {
    val tweak = Tweak.BooleanTweak(tweakId, tweakValue ?: releaseValue, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return BooleanTweakDelegate(tweakId)
}

fun intTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Int, minValue: Int, maxValue: Int, tweakValue: Int? = null, step: Int = 0): ReadOnlyProperty<Any, Int>  {
    val tweak = Tweak.IntTweak(tweakId, tweakValue ?: releaseValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return IntTweakDelegate(tweakId)
}

fun floatTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Float, minValue: Float, maxValue: Float, tweakValue: Float? = null, step: Float = 0F): ReadOnlyProperty<Any, Float>  {
    val tweak = Tweak.FloatTweak(tweakId, tweakValue ?: releaseValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return FloatTweakDelegate(tweakId)
}

fun doubleTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Double, minValue: Double, maxValue: Double, tweakValue: Double? = null, step: Double): ReadOnlyProperty<Any, Double>  {
    val tweak = Tweak.DoubleTweak(tweakId, tweakValue ?: releaseValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return DoubleTweakDelegate(tweakId)
}

fun longTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: Long, minValue: Long, maxValue: Long, tweakValue: Long? = null, step: Long): ReadOnlyProperty<Any, Long>  {
    val tweak = Tweak.LongTweak(tweakId, tweakValue ?: releaseValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return LongTweakDelegate(tweakId)
}

fun stringTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: String? = null, tweakValue: String? = null): ReadOnlyProperty<Any, String?> {
    val tweak = Tweak.StringTweak(tweakId, tweakValue, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return StringTweakDelegate(tweakId)
}

fun stringOptionsTweak(tweakId: String, group: String, tweakDescription: String, releaseValue: String, vararg otherOptions: String): ReadOnlyProperty<Any, String> {
    val options = setOf(releaseValue, *otherOptions)
    val tweak = Tweak.StringOptionsTweak(tweakId, releaseValue, options, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return StringOptionsTweakDelegate(tweakId)
}

fun stringResOptionsTweak(tweakId: String, group: String, tweakDescription: String, @StringRes releaseValue: Int, @StringRes vararg otherOptions: Int): ReadOnlyProperty<Any, Int> {
    val options = setOf(releaseValue, *otherOptions.toTypedArray())
    val tweak = Tweak.StringResOptionsTweak(tweakId, releaseValue, options, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return StringResOptionsTweakDelegate(tweakId)
}

fun registerActionTweak(tweakId: String, group: String, tweakDescription: String, tweakAction: (Context) -> Unit) {
    val tweak = Tweak.ActionTweak(tweakId, tweakAction, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
}