## Shaky Tweaks

This is a small Kotlin library to provide dynamic configurations on `debug` builds. App configuration can be
updated by shaking the device and changing the desired properties. 

Use `shakytweaks-noop` on `release` builds to disable dynamic configurations in production.

Usage
-----

Just two steps:

 1. Create any property or action by using one of `tweak` delegated properties
 2. Launch the App and shake the device to manage tweaks

Check out the sample app in `demo/` to see it in action.

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
----

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

Download
--------

```groovy
releaseImplementation "com.mindsea.shakytweaks:shakytweaks-noop:0.10-alpha"
debugImplementation "com.mindsea.shakytweaks:shakytweaks:0.10-alpha"
```


License
-------

    MIT License
    
    Copyright (c) 2019 MindSea
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
