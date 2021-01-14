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

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.mindsea.shakytweaks.ShakyTweaks
import com.mindsea.shakytweaks.registerActionTweak
import com.mindsea.shakytweaks.unregisterActionTweak
import kotlinx.android.synthetic.main.activity_main.*

private const val localActionKey = "local_action_key"

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        registerActionTweak(localActionKey, "Actions", "Display a button") {
            Toast.makeText(this, "Second Activity button visible!", Toast.LENGTH_LONG).show()
            button.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        val tweaks = TweakManager.instance
        booleanTweak.text = "login enabled = ${tweaks.featureFlagEnabled}"
        numbersTweak.text = """
            Int = ${tweaks.intTweak}
            Float = ${tweaks.floatTweak}
            Double = ${tweaks.doubleTweak}
            Long = ${tweaks.longTweak}
        """.trimIndent()
        stringTweak.text = "title = ${tweaks.title}"
        val server = getString(tweaks.server)
        stringResOptionsTweak.text = "Server = $server"
        stringOptionsTweak.text = "Message = ${tweaks.messageOptions}"
    }

    override fun onDestroy() {
        unregisterActionTweak(localActionKey)

        super.onDestroy()
    }
}
