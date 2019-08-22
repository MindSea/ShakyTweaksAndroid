package com.mindsea.shakytweaks

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class TweakDefaultValueDelegate<T>(private val defaultValue: T) : ReadOnlyProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T = defaultValue
}

@Suppress("UNUSED_PARAMETER")
fun booleanTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Boolean): ReadOnlyProperty<Any, Boolean> = TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun intTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Int, minValue: Int, maxValue: Int, step: Int = 0): ReadOnlyProperty<Any, Int> = TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun floatTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Float, minValue: Float, maxValue: Float, step: Float = 0F): ReadOnlyProperty<Any, Float> = TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun doubleTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Double, minValue: Double, maxValue: Double, step: Double): ReadOnlyProperty<Any, Double> = TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun longTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Long, minValue: Long, maxValue: Long, step: Long): ReadOnlyProperty<Any, Long> = TweakDefaultValueDelegate(defaultValue)

@Suppress("UNUSED_PARAMETER")
fun stringTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: String): ReadOnlyProperty<Any, String> = TweakDefaultValueDelegate(defaultValue)