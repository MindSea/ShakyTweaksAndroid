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

package com.mindsea

import android.content.Context
import android.widget.Toast
import com.mindsea.shakytweaks.*

class TweakManager {

    val featureFlagEnabled: Boolean by booleanTweak(
        tweakId = "login_enabled",
        group = "Feature Flags",
        tweakDescription = "Login Enabled",
        releaseValue = true,
        tweakValue = false
    )

    val intTweak: Int by intTweak(
        tweakId = "numbers_int",
        group = "Numbers",
        tweakDescription = "Int",
        releaseValue = 300,
        minValue = 0,
        maxValue = 1000,
        tweakValue = 50,
        step = 50
    )

    val floatTweak: Float by floatTweak(
        tweakId = "numbers_float",
        group = "Numbers",
        tweakDescription = "Float",
        releaseValue = 300f,
        minValue = 0f,
        maxValue = 1000f,
        tweakValue = 50f,
        step = 50f
    )

    val doubleTweak: Double by doubleTweak(
        tweakId = "numbers_double",
        group = "Numbers",
        tweakDescription = "Double",
        releaseValue = 300.0,
        minValue = 0.0,
        maxValue = 1000.0,
        tweakValue = 50.0,
        step = 50.0
    )

    val longTweak: Long by longTweak(
        tweakId = "numbers_long",
        group = "Numbers",
        tweakDescription = "Long",
        releaseValue = 300,
        minValue = 0,
        maxValue = 1000,
        tweakValue = 50,
        step = 50
    )

    val title: String? by stringTweak(
        tweakId = "title",
        group = "Strings",
        tweakDescription = "Title of main screen",
        releaseValue = null,
        tweakValue = "Tweaked"
    )

    val server: Int by stringResOptionsTweak(
        "server",
        "Server",
        "Server",
        R.string.prod_server,
        R.string.dev_server,
        R.string.stage_server
    )

    val messageOptions: String by stringOptionsTweak(
        "message_options",
        "String Options",
        "Welcome Message",
        "First Welcome Message",
        "Second Welcome Message",
        "Third Welcome Message"
    )

    fun init(context: Context) {
        registerActionTweak("action", "Actions", "Show a toast") {
            Toast.makeText(context, "Shaky Tweaks rocks!", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        val instance = TweakManager()
    }
}