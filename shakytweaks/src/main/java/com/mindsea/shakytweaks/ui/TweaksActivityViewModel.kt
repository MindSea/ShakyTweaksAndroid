package com.mindsea.shakytweaks.ui

internal class TweaksActivityViewModel {

    var onUpdate: ((TweaksActivityState) -> Unit)? = null
    set(value) {
        field = value
        value?.invoke(state)
    }

    private var state: TweaksActivityState = TweaksActivityState(TweakActivitySection.None, TweakActivitySection.Groups)
    set(value) {
        field = value
        onUpdate?.invoke(state)
    }

    fun backPressed() {
        state = when (state.currentSection) {
            is TweakActivitySection.Groups -> {
                state.copy(currentSection = TweakActivitySection.None, previousSection = state.currentSection)
            }
            is TweakActivitySection.Tweaks -> {
                state.copy(currentSection = TweakActivitySection.Groups, previousSection = state.currentSection)
            }
            else -> {
                throw IllegalStateException("Invalid state, must not get backPressed from ${state.currentSection}")
            }
        }
    }

    fun groupSelected(groupId: String) {
        state = state.copy(currentSection = TweakActivitySection.Tweaks(groupId), previousSection = state.currentSection)
    }
}

internal data class TweaksActivityState(val previousSection: TweakActivitySection, val currentSection: TweakActivitySection)

internal sealed class TweakActivitySection {

    object None : TweakActivitySection()

    object Groups : TweakActivitySection()

    data class Tweaks(val groupId: String) :  TweakActivitySection()
}