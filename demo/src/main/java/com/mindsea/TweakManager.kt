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