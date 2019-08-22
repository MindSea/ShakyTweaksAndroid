package com.mindsea.shakytweaks.ui.tweaks

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
                    is BooleanTweak -> BooleanTweakViewModel(tweak.id, tweak.description, tweakValueResolver)
                    is IntTweak, is LongTweak, is DoubleTweak, is FloatTweak -> NumberTweakViewModel(tweak.id, tweak.description, tweakValueResolver)
                    else -> throw UnsupportedOperationException("Not supported")
                }
            }
        state = state.copy(tweaks = tweaks)
    }

}

internal data class TweaksState(val tweaks: List<TweakItemViewModel>)

internal sealed class TweakItemViewModel {

    data class NumberTweakViewModel(private val tweakId: String, val description: String, private val tweakValueResolver: TweakValueResolver) : TweakItemViewModel() {
        val value: String
            get() = tweakValueResolver.getTypedValue<Number>(tweakId).toString()

        fun incrementValue() = tweakValueResolver.incrementValue(tweakId)

        fun decrementValue() = tweakValueResolver.decrementValue(tweakId)
    }

    data class BooleanTweakViewModel(private val tweakId: String, val description: String, private val tweakValueResolver: TweakValueResolver) : TweakItemViewModel() {
        val value: Boolean
            get() = tweakValueResolver.getTypedValue(tweakId)

        fun setValue(value: Boolean) {
            tweakValueResolver.updateValue(tweakId, value)
        }
    }
}