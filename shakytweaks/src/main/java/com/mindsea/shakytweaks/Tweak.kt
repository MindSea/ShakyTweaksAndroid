package com.mindsea.shakytweaks

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal sealed class Tweak {

    abstract val id: String

    abstract val group: String

    abstract val description: String

    data class BooleanTweak(
        override val id: String,
        val defaultValue: Boolean,
        override val group: String,
        override val description: String) : Tweak()

    data class IntTweak(
        override val id: String,
        val defaultValue: Int,
        val minValue: Int,
        val maxValue: Int,
        val step: Int,
        override val group: String,
        override val description: String) : Tweak()

    data class DoubleTweak(
        override val id: String,
        val defaultValue: Double,
        val minValue: Double,
        val maxValue: Double,
        val step: Double,
        override val group: String,
        override val description: String) : Tweak()

    data class LongTweak(
        override val id: String,
        val defaultValue: Long,
        val minValue: Long,
        val maxValue: Long,
        val step: Long,
        override val group: String,
        override val description: String) : Tweak()

    data class FloatTweak(
        override val id: String,
        val defaultValue: Float,
        val minValue: Float,
        val maxValue: Float,
        val step: Float,
        override val group: String,
        override val description: String) : Tweak()

    data class StringTweak(
        override val id: String,
        val defaultValue: String,
        override val group: String,
        override val description: String) : Tweak()

}

private val tweakProvider = TweakProvider.instance

private class BooleanTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean = tweakProvider.getTypedValue(tweakId)
}

private class IntTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int = tweakProvider.getTypedValue(tweakId)
}

private class FloatTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Float> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Float = tweakProvider.getTypedValue(tweakId)
}

private class DoubleTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Double> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Double = tweakProvider.getTypedValue(tweakId)
}

private class LongTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, Long> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Long = tweakProvider.getTypedValue(tweakId)
}

private class StringTweakDelegate(private val tweakId: String) : ReadOnlyProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String = tweakProvider.getTypedValue(tweakId)
}

fun booleanTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Boolean): ReadOnlyProperty<Any, Boolean> {
    val tweak = Tweak.BooleanTweak(tweakId, defaultValue, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return BooleanTweakDelegate(tweakId)
}

fun intTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Int, minValue: Int, maxValue: Int, step: Int = 0): ReadOnlyProperty<Any, Int>  {
    val tweak = Tweak.IntTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return IntTweakDelegate(tweakId)
}

fun floatTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Float, minValue: Float, maxValue: Float, step: Float = 0F): ReadOnlyProperty<Any, Float>  {
    val tweak = Tweak.FloatTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return FloatTweakDelegate(tweakId)
}

fun doubleTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Double, minValue: Double, maxValue: Double, step: Double): ReadOnlyProperty<Any, Double>  {
    val tweak = Tweak.DoubleTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return DoubleTweakDelegate(tweakId)
}

fun longTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Long, minValue: Long, maxValue: Long, step: Long): ReadOnlyProperty<Any, Long>  {
    val tweak = Tweak.LongTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return LongTweakDelegate(tweakId)
}

fun stringTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: String): ReadOnlyProperty<Any, String> {
    val tweak = Tweak.StringTweak(tweakId, defaultValue, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return StringTweakDelegate(tweakId)
}