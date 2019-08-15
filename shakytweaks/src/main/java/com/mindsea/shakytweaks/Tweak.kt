package com.mindsea.shakytweaks

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

fun booleanTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Boolean): () -> Boolean {
    val tweak = Tweak.BooleanTweak(tweakId, defaultValue, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return getTweakValue(tweakId)
}

fun intTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Int, minValue: Int, maxValue: Int, step: Int = 0): () -> Int {
    val tweak = Tweak.IntTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return getTweakValue(tweakId)
}

fun floatTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Float, minValue: Float, maxValue: Float, step: Float = 0F): () -> Float {
    val tweak = Tweak.FloatTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return getTweakValue(tweakId)
}

fun doubleTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Double, minValue: Double, maxValue: Double, step: Double): () -> Double {
    val tweak = Tweak.DoubleTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return getTweakValue(tweakId)
}

fun longTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: Long, minValue: Long, maxValue: Long, step: Long): () -> Long {
    val tweak = Tweak.LongTweak(tweakId, defaultValue, minValue, maxValue, step, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return getTweakValue(tweakId)
}

fun stringTweak(tweakId: String, group: String, tweakDescription: String, defaultValue: String): () -> String {
    val tweak = Tweak.StringTweak(tweakId, defaultValue, group, tweakDescription)
    tweakProvider.storeTweak(tweakId, tweak)
    return getTweakValue(tweakId)
}

private inline fun <reified T> getTweakValue(key: String): () -> T {
    return {
        tweakProvider.getTypedValue(key)
    }
}