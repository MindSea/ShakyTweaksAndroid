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

package com.mindsea.shakytweaks.ui.tweaks

import android.content.Context
import com.mindsea.shakytweaks.Tweak.*
import com.mindsea.shakytweaks.TweakProvider
import com.mindsea.shakytweaks.TweakValueResolver
import com.mindsea.shakytweaks.ui.tweaks.TweakItemViewModel.*

internal class TweaksViewModel(
    tweakProvider: TweakProvider,
    private val tweakValueResolver: TweakValueResolver,
    groupId: String
) {

    var onUpdate: ((TweaksState) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(state)
        }

    private var state: TweaksState = TweaksState(emptyList())
        set(value) {
            field = value
            onUpdate?.invoke(value)
        }

    init {
        val tweaks = tweakProvider.tweaks.filter { it.group == groupId }
            .map { tweak ->
                when (tweak) {
                    is BooleanTweak -> BooleanTweakViewModel(
                        tweak.id,
                        tweak.description,
                        tweakValueResolver
                    )
                    is IntTweak, is LongTweak, is DoubleTweak, is FloatTweak -> NumberTweakViewModel(
                        tweak.id,
                        tweak.description,
                        tweakValueResolver
                    )
                    is StringTweak -> StringTweakViewModel(
                        tweak.id,
                        tweak.description,
                        tweakValueResolver
                    )
                    is StringOptionsTweak -> StringOptionsTweakViewModel(
                        tweak.id,
                        tweak.description,
                        tweak.options.toList(),
                        tweakValueResolver
                    )
                    is StringResOptionsTweak -> StringResOptionsTweakViewModel(
                        tweak.id,
                        tweak.description,
                        tweak.options.toList(),
                        tweakValueResolver
                    )
                    is ActionTweak -> ActionTweakViewModel(
                        tweak.id,
                        tweak.description,
                        tweakValueResolver
                    )
                }
            }
        state = state.copy(tweaks = tweaks)
    }

}

internal data class TweaksState(val tweaks: List<TweakItemViewModel>)

internal sealed class TweakItemViewModel {

    data class NumberTweakViewModel(
        private val tweakId: String,
        val description: String,
        private val tweakValueResolver: TweakValueResolver
    ) : TweakItemViewModel() {
        val value: String
            get() = tweakValueResolver.getTypedValue<Number>(tweakId).toString()

        fun incrementValue() = tweakValueResolver.incrementValue(tweakId)

        fun decrementValue() = tweakValueResolver.decrementValue(tweakId)
    }

    data class BooleanTweakViewModel(
        private val tweakId: String,
        val description: String,
        private val tweakValueResolver: TweakValueResolver
    ) : TweakItemViewModel() {
        val value: Boolean
            get() = tweakValueResolver.getTypedValue(tweakId)

        fun setValue(value: Boolean) {
            tweakValueResolver.updateValue(tweakId, value)
        }
    }

    data class StringTweakViewModel(
        private val tweakId: String,
        val description: String,
        private val tweakValueResolver: TweakValueResolver
    ) : TweakItemViewModel() {
        val value: String
            get() = tweakValueResolver.getTypedValue(tweakId)

        fun setValue(value: String) {
            tweakValueResolver.updateValue(tweakId, value)
        }
    }

    data class StringOptionsTweakViewModel(
        private val tweakId: String,
        val description: String,
        val options: List<String>,
        private val tweakValueResolver: TweakValueResolver
    ) : TweakItemViewModel() {
        val selectedIndex: Int
            get() {
                val value = tweakValueResolver.getTypedValue<String>(tweakId)
                return options.indexOf(value)
            }

        fun setValueAtIndex(index: Int) {
            val value = options[index]
            tweakValueResolver.updateValue(tweakId, value)
        }
    }

    data class StringResOptionsTweakViewModel(
        private val tweakId: String,
        val description: String,
        val options: List<Int>,
        private val tweakValueResolver: TweakValueResolver
    ) : TweakItemViewModel() {
        val selectedIndex: Int
            get() {
                val value = tweakValueResolver.getTypedValue<Int>(tweakId)
                return options.indexOf(value)
            }

        fun setValueAtIndex(index: Int) {
            val value = options[index]
            tweakValueResolver.updateValue(tweakId, value)
        }
    }

    data class ActionTweakViewModel(
        private val tweakId: String,
        val description: String,
        private val tweakValueResolver: TweakValueResolver
    ) : TweakItemViewModel() {
        val value: () -> Unit
            get() = tweakValueResolver.getTypedValue(tweakId)
    }
}