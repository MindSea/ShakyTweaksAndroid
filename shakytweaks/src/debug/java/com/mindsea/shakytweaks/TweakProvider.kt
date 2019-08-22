package com.mindsea.shakytweaks

import android.util.Log

internal class TweakProvider {

    private val tweakMap: MutableMap<String, Tweak> = mutableMapOf()

    val tweaks: List<Tweak>
    get() = tweakMap.map { it.value }

    fun storeTweak(key: String, tweak: Tweak) {
        if (tweakMap.containsKey(key)) {
            throw IllegalStateException("Tweak key must be unique. Key $key was already added")
        }
        Log.d("ShakyTweaks", "Storing $tweak with key $key")
        tweakMap[key] = tweak
    }

    fun findTweak(key: String): Tweak {
        return tweakMap[key] ?: throw IllegalStateException("Could not find tweak with key $key")
    }
}