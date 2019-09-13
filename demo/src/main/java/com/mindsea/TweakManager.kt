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

import com.mindsea.shakytweaks.*

class TweakManager {

    val featureFlagEnabled: Boolean by booleanTweak("login_enabled", "Feature Flags", "Login Enabled", true)

    val animationDuration: Int by intTweak("animation_duration", "Animation Durations", "Fade Animation Duration", 300, 0, 1000, 50)

    val title: String by stringTweak("title", "Strings", "Title of main screen", "Default Title")

    val server: Int by stringResOptionsTweak("server", "Server", "Server", R.string.prod_server, R.string.dev_server, R.string.stage_server)

    val messageOptions: String by stringOptionsTweak("message_options", "String Options", "Welcome Message", "First Welcome Message", "Second Welcome Message", "Third Welcome Message" )

    companion object {
        val instance = TweakManager()
    }
}