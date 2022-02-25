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

import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.TweakValueResolver
import com.mindsea.shakytweaks.databinding.*
import com.mindsea.shakytweaks.ui.tweaks.TweakItemViewModel.*

private const val NUMERIC_VIEW_TYPE = 0
private const val BOOLEAN_VIEW_TYPE = 1
private const val STRING_VIEW_TYPE = 2
private const val OPTIONS_VIEW_TYPE = 3
private const val ACTION_VIEW_TYPE = 4

internal class TweaksAdapter : RecyclerView.Adapter<TweakViewHolder>() {

    private val tweakItemViewModels = mutableListOf<TweakItemViewModel>()

    fun setTweaks(tweaks: List<TweakItemViewModel>) {
        this.tweakItemViewModels.clear()
        this.tweakItemViewModels.addAll(tweaks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweakViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewBinding = when (viewType) {
            NUMERIC_VIEW_TYPE -> ItemNumericTweakBinding.inflate(inflater, parent, false)
            BOOLEAN_VIEW_TYPE -> ItemBooleanTweakBinding.inflate(inflater, parent, false)
            STRING_VIEW_TYPE -> ItemStringTweakBinding.inflate(inflater, parent, false)
            OPTIONS_VIEW_TYPE -> ItemOptionsTweakBinding.inflate(inflater, parent, false)
            ACTION_VIEW_TYPE -> ItemActionTweakBinding.inflate(inflater, parent, false)
            else -> throw java.lang.UnsupportedOperationException("not implemented")
        }
        
        return TweakViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return when (tweakItemViewModels[position]) {
            is BooleanTweakViewModel -> BOOLEAN_VIEW_TYPE
            is NumberTweakViewModel -> NUMERIC_VIEW_TYPE
            is StringTweakViewModel -> STRING_VIEW_TYPE
            is StringOptionsTweakViewModel, is StringResOptionsTweakViewModel -> OPTIONS_VIEW_TYPE
            is ActionTweakViewModel -> ACTION_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return tweakItemViewModels.size
    }

    override fun onBindViewHolder(holder: TweakViewHolder, position: Int) {
        when (val itemViewModel = tweakItemViewModels[position]) {
            is BooleanTweakViewModel -> bindBooleanTweakView(holder, itemViewModel)
            is NumberTweakViewModel -> bindNumberTweakView(holder, itemViewModel)
            is StringTweakViewModel -> bindStringTweakView(holder, itemViewModel)
            is StringOptionsTweakViewModel -> bindStringOptionsTweakView(holder, itemViewModel)
            is StringResOptionsTweakViewModel -> bindStringOptionTweakView(holder, itemViewModel)
            is ActionTweakViewModel -> bindActionTweakView(holder, itemViewModel)
        }
    }

    private fun bindBooleanTweakView(holder: TweakViewHolder, itemViewModel: BooleanTweakViewModel) {
        val binding = holder.binding as ItemBooleanTweakBinding
        binding.tweakDescription.text = itemViewModel.description
        binding.enabledSwitch.isChecked = itemViewModel.value
        binding.enabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            itemViewModel.setValue(isChecked)
        }
    }

    private fun bindNumberTweakView(holder: TweakViewHolder, itemViewModel: NumberTweakViewModel) {
        val binding = holder.binding as ItemNumericTweakBinding
        binding.tweakDescription.text = itemViewModel.description
        binding.numericTweakText.setText(itemViewModel.value)
        binding.numericTweakText.inputType = when (itemViewModel.allowsDecimals) {
            true -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            false -> InputType.TYPE_CLASS_NUMBER
        }
        if (itemViewModel.allowsNegativeNumbers) {
            binding.numericTweakText.inputType =
                binding.numericTweakText.inputType or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        binding.incrementButton.setOnClickListener {
            itemViewModel.incrementValue()
            binding.numericTweakText.setText(itemViewModel.value)
        }
        binding.decrementButton.setOnClickListener {
            itemViewModel.decrementValue()
            binding.numericTweakText.setText(itemViewModel.value)
        }
        binding.numericTweakText.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    (v as? EditText)?.let {
                        try {
                            itemViewModel.setValueFromString(it.text.toString())
                        } catch (e: java.lang.NumberFormatException) {
                            binding.numericTweakText.setText(itemViewModel.value)
                        } catch (e: TweakValueResolver.TweakValueOutOfRangeException) {
                            binding.numericTweakText.setText(itemViewModel.value)
                            Toast.makeText(
                                it.context, when (e.thresholdType) {
                                    TweakValueResolver.TweakValueOutOfRangeException.ThresholdType.MAX -> it.context.getString(
                                        R.string.tweak_value_too_large_alert,
                                        e.attemptedValue,
                                        e.thresholdValue
                                    )
                                    TweakValueResolver.TweakValueOutOfRangeException.ThresholdType.MIN -> it.context.getString(
                                        R.string.tweak_value_too_small_alert,
                                        e.attemptedValue,
                                        e.thresholdValue
                                    )
                                }, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        })
    }

    private fun bindStringTweakView(holder: TweakViewHolder, itemViewModel: StringTweakViewModel) {
        val binding = holder.binding as ItemStringTweakBinding
        binding.tweakDescription.text = itemViewModel.description
        binding.tweakText.setText(itemViewModel.value)
        binding.tweakText.addTextChangedListener(object : TextWatcher {
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

    private fun bindStringOptionsTweakView(
        holder: TweakViewHolder,
        itemViewModel: StringOptionsTweakViewModel
    ) {
        val context = holder.itemView.context
        val binding = holder.binding as ItemOptionsTweakBinding
        binding.tweakDescription.text = itemViewModel.description
        val radioButtons = itemViewModel.options.map { option ->
            RadioButton(context).apply {
                id = View.generateViewId()
                text = option
            }
        }
        val viewIds = radioButtons.map { it.id }
        radioButtons.forEach { radioButton ->
            binding.tweakOptions.addView(radioButton)
        }
        binding.tweakOptions.check(viewIds[itemViewModel.selectedIndex])
        binding.tweakOptions.setOnCheckedChangeListener { _, checkedId ->
            val index = viewIds.indexOf(checkedId)
            itemViewModel.setValueAtIndex(index)
        }
    }

    @SuppressLint("ResourceType")
    private fun bindStringOptionTweakView(
        holder: TweakViewHolder,
        itemViewModel: StringResOptionsTweakViewModel
    ) {
        val context = holder.itemView.context
        val binding = holder.binding as ItemOptionsTweakBinding
        binding.tweakDescription.text = itemViewModel.description
        val radioButtons = itemViewModel.options.map { stringId ->
            RadioButton(context).apply {
                id = View.generateViewId()
                text = context.getString(stringId)
            }
        }
        val viewIds = radioButtons.map { it.id }
        radioButtons.forEach { radioButton ->
            binding.tweakOptions.addView(radioButton)
        }
        binding.tweakOptions.check(viewIds[itemViewModel.selectedIndex])
        binding.tweakOptions.setOnCheckedChangeListener { _, checkedId ->
            val index = viewIds.indexOf(checkedId)
            itemViewModel.setValueAtIndex(index)
        }
    }

    private fun bindActionTweakView(holder: TweakViewHolder, itemViewModel: ActionTweakViewModel) {
        val binding = holder.binding as ItemActionTweakBinding
        binding.tweakDescription.text = itemViewModel.description
        binding.actionButton.setOnClickListener {
            itemViewModel.value.invoke()
        }
    }
}

internal class TweakViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)