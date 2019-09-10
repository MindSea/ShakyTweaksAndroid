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

package com.mindsea.shakytweaks.ui.tweakgroups

import com.mindsea.shakytweaks.TweakProvider

internal class TweakGroupsViewModel(tweakProvider: TweakProvider) {

    private var state: TweakGroupsState =
        TweakGroupsState(emptyList())
    set(value) {
        field = value
        onUpdate?.invoke(value)
    }

    var onUpdate: ((TweakGroupsState) -> Unit)? = null
    set(value) {
        field = value
        value?.invoke(state)
    }

    init {
        val groups = tweakProvider.tweaks
            .distinctBy { it.group }
            .map { TweakGroup(it.group) }
        state = TweakGroupsState(groups)
    }
}

internal data class TweakGroupsState(val groups: List<TweakGroup>)

internal data class TweakGroup(val name: String)