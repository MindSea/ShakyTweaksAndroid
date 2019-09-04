package com.mindsea.shakytweaks.ui.tweaks

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ui.tweaks.TweakItemViewModel.*
import kotlinx.android.synthetic.debug.item_boolean_tweak.view.*
import kotlinx.android.synthetic.debug.item_numeric_tweak.view.*
import kotlinx.android.synthetic.debug.item_numeric_tweak.view.tweakDescription
import kotlinx.android.synthetic.main.item_string_tweak.view.*

private const val NUMERIC_VIEW_TYPE = 0
private const val BOOLEAN_VIEW_TYPE = 1
private const val STRING_VIEW_TYPE = 2

internal class TweaksAdapter : RecyclerView.Adapter<TweakViewHolder>() {

    private val tweakItemViewModels = mutableListOf<TweakItemViewModel>()

    fun setTweaks(tweaks: List<TweakItemViewModel>) {
        this.tweakItemViewModels.clear()
        this.tweakItemViewModels.addAll(tweaks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweakViewHolder {
        @LayoutRes val layoutId: Int = when (viewType) {
            NUMERIC_VIEW_TYPE -> R.layout.item_numeric_tweak
            BOOLEAN_VIEW_TYPE -> R.layout.item_boolean_tweak
            STRING_VIEW_TYPE -> R.layout.item_string_tweak
            else -> throw UnsupportedOperationException("not implemented")
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return TweakViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when (tweakItemViewModels[position]) {
            is BooleanTweakViewModel -> BOOLEAN_VIEW_TYPE
            is NumberTweakViewModel -> NUMERIC_VIEW_TYPE
            is StringTweakViewModel -> STRING_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return tweakItemViewModels.size
    }

    override fun onBindViewHolder(holder: TweakViewHolder, position: Int) {
        val itemViewModel = tweakItemViewModels[position]
        val itemView = holder.itemView

        when (itemViewModel) {
            is BooleanTweakViewModel -> {
                itemView.tweakDescription.text = itemViewModel.description
                itemView.enabledSwitch.isChecked = itemViewModel.value
                itemView.enabledSwitch.setOnCheckedChangeListener { _, isChecked ->
                    itemViewModel.setValue(isChecked)
                }
            }
            is NumberTweakViewModel -> {
                itemView.tweakDescription.text = itemViewModel.description
                itemView.tweakValue.text = itemViewModel.value
                itemView.incrementButton.setOnClickListener {
                    itemViewModel.incrementValue()
                    itemView.tweakValue.text = itemViewModel.value
                }
                itemView.decrementButton.setOnClickListener {
                    itemViewModel.decrementValue()
                    itemView.tweakValue.text = itemViewModel.value
                }
            }
            is StringTweakViewModel -> {
                itemView.tweakDescription.text = itemViewModel.description
                itemView.tweakText.setText(itemViewModel.value)
                itemView.tweakText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        // Do nothing
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        // Do nothing
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        itemViewModel.setValue(s?.toString() ?: "")
                    }

                })
            }
        }
    }
}

internal class TweakViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)