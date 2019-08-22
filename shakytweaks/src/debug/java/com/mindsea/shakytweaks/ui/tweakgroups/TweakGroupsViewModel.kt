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