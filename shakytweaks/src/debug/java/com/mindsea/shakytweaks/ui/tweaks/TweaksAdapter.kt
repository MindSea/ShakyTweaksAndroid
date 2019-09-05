package com.mindsea.shakytweaks.ui.tweaks

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ui.tweaks.TweakItemViewModel.*
import kotlinx.android.synthetic.debug.item_boolean_tweak.view.*
import kotlinx.android.synthetic.debug.item_numeric_tweak.view.*
import kotlinx.android.synthetic.debug.item_numeric_tweak.view.tweakDescription
import kotlinx.android.synthetic.main.item_options_tweak.view.*
import kotlinx.android.synthetic.main.item_string_tweak.view.*

private const val NUMERIC_VIEW_TYPE = 0
private const val BOOLEAN_VIEW_TYPE = 1
private const val STRING_VIEW_TYPE = 2
private const val OPTIONS_VIEW_TYPE = 3

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
            OPTIONS_VIEW_TYPE -> R.layout.item_options_tweak
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
            is StringOptionsTweakViewModel, is StringResOptionsTweakViewModel -> OPTIONS_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return tweakItemViewModels.size
    }

    override fun onBindViewHolder(holder: TweakViewHolder, position: Int) {
        val itemViewModel = tweakItemViewModels[position]
        val itemView = holder.itemView

        when (itemViewModel) {
            is BooleanTweakViewModel -> bindBooleanTweakView(itemView, itemViewModel)
            is NumberTweakViewModel -> bindNumberTweakView(itemView, itemViewModel)
            is StringTweakViewModel -> bindStringTweakView(itemView, itemViewModel)
            is StringOptionsTweakViewModel -> bindStringOptionsTweakView(itemView, itemViewModel)
            is StringResOptionsTweakViewModel -> bindStringOptionTweakView(itemView, itemViewModel)

        }
    }

    private fun bindBooleanTweakView(itemView: View, itemViewModel: BooleanTweakViewModel) {
        itemView.tweakDescription.text = itemViewModel.description
        itemView.enabledSwitch.isChecked = itemViewModel.value
        itemView.enabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            itemViewModel.setValue(isChecked)
        }
    }

    private fun bindNumberTweakView(itemView: View, itemViewModel: NumberTweakViewModel) {
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

    private fun bindStringTweakView(itemView: View, itemViewModel: StringTweakViewModel) {
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

    private fun bindStringOptionsTweakView(itemView: View, itemViewModel: StringOptionsTweakViewModel) {
        val context = itemView.context
        itemView.tweakDescription.text = itemViewModel.description
        val radioButtons = itemViewModel.options.map { option ->
            RadioButton(context).apply {
                id = View.generateViewId()
                text = option
            }
        }
        val viewIds = radioButtons.map { it.id }
        radioButtons.forEach { radioButton ->
            itemView.tweakOptions.addView(radioButton)
        }
        itemView.tweakOptions.check(viewIds[itemViewModel.selectedIndex])
        itemView.tweakOptions.setOnCheckedChangeListener { _, checkedId ->
            val index = viewIds.indexOf(checkedId)
            itemViewModel.setValueAtIndex(index)
        }
    }

    @SuppressLint("ResourceType")
    private fun bindStringOptionTweakView(itemView: View, itemViewModel: StringResOptionsTweakViewModel) {
        val context = itemView.context
        itemView.tweakDescription.text = itemViewModel.description
        val radioButtons = itemViewModel.options.map { stringId ->
            RadioButton(context).apply {
                id = View.generateViewId()
                text = context.getString(stringId)
            }
        }
        val viewIds = radioButtons.map { it.id }
        radioButtons.forEach { radioButton ->
            itemView.tweakOptions.addView(radioButton)
        }
        itemView.tweakOptions.check(viewIds[itemViewModel.selectedIndex])
        itemView.tweakOptions.setOnCheckedChangeListener { _, checkedId ->
            val index = viewIds.indexOf(checkedId)
            itemViewModel.setValueAtIndex(index)
        }
    }
}

internal class TweakViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)