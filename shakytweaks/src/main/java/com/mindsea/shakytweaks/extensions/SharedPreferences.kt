package com.mindsea.shakytweaks.extensions

import android.content.SharedPreferences

fun SharedPreferences.Editor.putDouble(key: String, value: Double): SharedPreferences.Editor {
    return this.putLong(key, value.toLong())
}

fun SharedPreferences.getDouble(key: String, defaultValue: Double): Double {
    return this.getLong(key, defaultValue.toLong()).toDouble()
}