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

package com.mindsea.shakytweaks.ui

internal class TweaksActivityViewModel {

    var onUpdate: ((TweaksActivityState) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(state)
        }

    private var state: TweaksActivityState =
        TweaksActivityState(TweakActivitySection.None, TweakActivitySection.Groups)
        set(value) {
            field = value
            onUpdate?.invoke(state)
        }

    fun backPressed() {
        state = when (state.currentSection) {
            is TweakActivitySection.Groups -> {
                state.copy(
                    currentSection = TweakActivitySection.None,
                    previousSection = state.currentSection
                )
            }
            is TweakActivitySection.Tweaks -> {
                state.copy(
                    currentSection = TweakActivitySection.Groups,
                    previousSection = state.currentSection
                )
            }
            else -> {
                throw IllegalStateException("Invalid state, must not get backPressed from ${state.currentSection}")
            }
        }
    }

    fun groupSelected(groupId: String) {
        state = state.copy(
            currentSection = TweakActivitySection.Tweaks(groupId),
            previousSection = state.currentSection
        )
    }
}

internal data class TweaksActivityState(
    val previousSection: TweakActivitySection,
    val currentSection: TweakActivitySection
)

internal sealed class TweakActivitySection {

    object None : TweakActivitySection()

    object Groups : TweakActivitySection()

    data class Tweaks(val groupId: String) : TweakActivitySection()
}