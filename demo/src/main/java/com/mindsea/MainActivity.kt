package com.mindsea

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val tweaks = TweakManager.instance
        booleanTweak.text = "login enabled = ${tweaks.featureFlagEnabled}"
        intTweak.text = "animation duration = ${tweaks.animationDuration}"
        stringTweak.text = "title = ${tweaks.title}"
        val server = getString(tweaks.server)
        stringResOptionsTweak.text = "Server = $server"
        stringOptionsTweak.text = "Message = ${tweaks.messageOptions}"
    }
}
