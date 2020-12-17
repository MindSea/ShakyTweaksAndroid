package com.mindsea.shakytweaks

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle

object ShakyTweaks {

    @Deprecated("Issues related to the lifecycle.", replaceWith = ReplaceWith("init(activity: Activity, lifecycle: Lifecycle)"))
    @Suppress("UNUSED_PARAMETER")
    fun init(context: Context) {
        // Do nothing
    }

    @Suppress("UNUSED_PARAMETER")
    fun init(activity: Activity, lifecycle: Lifecycle) {
        // Do nothing
    }

    @Suppress("UNUSED_PARAMETER")
    fun onKeyDown(context: Context, keyCode: Int) {
        // Do nothing
    }
}