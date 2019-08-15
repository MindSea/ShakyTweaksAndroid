package com.mindsea

import com.mindsea.shakytweaks.booleanTweak
import com.mindsea.shakytweaks.intTweak

class TweakManager {

    val featureFlagEnabled: Boolean by booleanTweak("login_enabled", "Feature Flags", "Login Enabled", true)

    val animationDuration: Int by intTweak("animation_duration", "Animation Durations", "Fade Animation Duration", 300, 0, 1000, 50)

    companion object {
        val instance = TweakManager()
    }
}