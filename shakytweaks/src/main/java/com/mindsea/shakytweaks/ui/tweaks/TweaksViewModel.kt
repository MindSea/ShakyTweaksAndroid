package com.mindsea.shakytweaks.ui.tweaks

import com.mindsea.shakytweaks.Tweak.*
import com.mindsea.shakytweaks.TweakProvider
import com.mindsea.shakytweaks.ui.tweaks.TweakItemViewModel.*

internal class TweaksViewModel(
    private val tweakProvider: TweakProvider,
    groupId: String
) : TweakVisitor {

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
                    is BooleanTweak -> BooleanTweakViewModel(tweak.id, tweak.description, tweakProvider)
                    is IntTweak, is LongTweak, is DoubleTweak, is FloatTweak -> NumberTweakViewModel(tweak.id, tweak.description, tweakProvider)
                    else -> throw UnsupportedOperationException("Not supported")
                }
            }
        state = state.copy(tweaks = tweaks)
    }

    override fun onValueChanged(tweak: BooleanTweak, value: Boolean) {
        tweakProvider.updateValue("login_enabled", value)
    }

    override fun onValueChanged(tweak: StringTweak, value: String) {

    }

    override fun onValueIncrementedBy(tweak: IntTweak, increment: Int) {

    }

}

internal data class TweaksState(val tweaks: List<TweakItemViewModel>)

internal sealed class TweakItemViewModel {

    data class NumberTweakViewModel(private val tweakId: String, val description: String, private val tweakProvider: TweakProvider) : TweakItemViewModel() {
        val value: String
            get() = tweakProvider.getTypedValue<Number>(tweakId).toString()

        fun incrementValue() = tweakProvider.incrementValue(tweakId)

        fun decrementValue() = tweakProvider.decrementValue(tweakId)
    }

    data class BooleanTweakViewModel(private val tweakId: String, val description: String, private val tweakProvider: TweakProvider) : TweakItemViewModel() {
        val value: Boolean
            get() = tweakProvider.getTypedValue(tweakId)

        fun setValue(value: Boolean) {
            tweakProvider.updateValue(tweakId, value)
        }
    }
}