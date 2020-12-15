# Shaky Tweaks Android
[![](https://jitpack.io/v/MindSea/ShakyTweaksAndroid.svg)](https://jitpack.io/#MindSea/ShakyTweaksAndroid)

A small Kotlin library to provide dynamic configurations on test builds. The App's configuration can be
updated on the fly with Shaky Tweaks.

Usage
-----
Add the JitPack repository to your root build.gradle:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependencies on your app module. Use the `no-op` variant on your release builds, so that Tweaks are disabled on production builds
```
repositories {
    ...
    def shaky_tweaks_version = 'x.x.x'
    releaseImplementation "com.github.mindsea.shakytweaksandroid:shakytweaks-noop:$shaky_tweaks_version"
    debugImplementation "com.github.mindsea.shakytweaksandroid:shakytweaks:$shaky_tweaks_version"
}
```

Create a `TweakManager` class, and describe your tweakable variables
```kotlin
object TweakManager {
    val tweakedBoolean: Boolean by booleanTweak(
        tweakId = "unique_identifier",
        group = "Group Name",
        tweakDescription = "A description...",
        releaseValue = true, // value for release builds
        defaultTweakValue = false // value for debug builds
    )
}
```

On your `BaseActivity` class, initialize Shaky Tweaks.
```kotlin
class BaseActivity : AppCompatActivity(), LifecycleOwner {
    override fun onCreate() {
        super.onCreate()
        ...    
        ShakyTweaks.init(this, lifecycle)
    }
    
    override fun onDestroy() {
        ShakyTweaks.release(this@MainActivity)
        ...
        super.onDestroy()
    }
}
```

Read the current assigned tweak value
```kotlin
Toast.makeText(context, "Shaky Tweaks boolean is: ${TweakManager.tweakedBoolean}", Toast.LENGTH_LONG).show()
```

To change the Tweak values on fly, simply shake your device to access the Shaky Tweaks screen.



###  Emulator Usage

To enable easy access to the Shaky Tweaks menu from an Android Emulator, you should forward `onKeyDown` from your activities to `ShakyTweaks.onKeyDown`
```kotlin
class BaseActivity : AppCompatActivity() {
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        ShakyTweaks.onKeyDown(this, keyCode)
        return super.onKeyDown(keyCode, event)
    }
}
```

You can then access the Shaky Tweaks menu by pressing `S` + `T` simultaneously from the activities that forward `onKeyDown`.
The shortcut doesn't work if an input field is active.

Samples
-------

Check out the sample app in `demo/` to see it in action.

Shaky Tweaks has support for the following types

* Boolean
```kotlin
val aBooleanValue: Boolean by booleanTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    releaseValue = true, // value for release builds
    defaultTweakValue = false // value for debug builds
)
```

* Int
```kotlin
val intValue: Int by intTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    releaseValue = 300,
    minValue = 0,
    maxValue = 1000,
    defaultTweakValue = 50,
    step = 50 // increment by
)
```

* Float
```kotlin
val floatValue: Float by floatTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    releaseValue = 300f,
    minValue = 0f,
    maxValue = 1000f,
    defaultTweakValue = 50f,
    step = 50f
)
```

* Double
```kotlin
val doubleValue: Double by doubleTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    releaseValue = 300.0,
    minValue = 0.0,
    maxValue = 1000.0,
    defaultTweakValue = 50.0,
    step = 50.0
)
```

* Long
```kotlin
val longValue: Long by longTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    releaseValue = 300,
    minValue = 0,
    maxValue = 1000,
    defaultTweakValue = 50,
    step = 50
)
```

* String
```kotlin
val stringValue: String? by stringTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    releaseValue = null,
    defaultTweakValue = "Tweaked"
)

val stringResOptionValue: Int by stringResOptionsTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    R.string.prod_server,
    R.string.dev_server,
    R.string.stage_server
)

val stringOptionValue: String by stringOptionsTweak(
    tweakId = "unique_identifier",
    group = "Group Name",
    tweakDescription = "A description...",
    "First Welcome Message",
    "Second Welcome Message",
    "Third Welcome Message"
)
```

* Action
```kotlin
registerActionTweak("action", "Actions", "Show a toast") {
        Toast.makeText(context, "Shaky Tweaks rocks!", Toast.LENGTH_LONG).show()
    }
```

SNAPSHOT builds:
----------------
This repo support snapshot builds. Snapshots are built over specific branches.
Simply change the version you are importing to `{branch}-SNAPSHOT`.

*Note:*

It might be necessary to update your Gradle settings to disable cache so that you always get the latest SNAPSHOT.
* Update your project configuration:
    ```
    configurations.all {
        ...
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
    }
    ```
* Update how you import the lib:
    ```
    debugImplementation group: "com.github.mindsea.shakytweaksandroid", name: "shakytweaks-noop", version: "mybranch-SNAPSHOT", changing: true  
    ```

Next proposed features
----------------------
* Implement `stringTweak` and `stringOptionalTweak` to better discriminate where the tweak is is optional or not.
  * experiment generic tweak definition: `tweak<String>` and `tweak<String?>`
