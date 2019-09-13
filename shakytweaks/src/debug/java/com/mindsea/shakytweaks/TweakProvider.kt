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