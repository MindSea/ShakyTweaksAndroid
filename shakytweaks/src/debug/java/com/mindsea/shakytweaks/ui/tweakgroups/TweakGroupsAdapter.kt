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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mindsea.shakytweaks.R
import kotlinx.android.synthetic.debug.item_tweak_group.view.*

internal class TweakGroupsAdapter : RecyclerView.Adapter<TweakGroupViewHolder>() {

    private val tweakGroups = mutableListOf<TweakGroup>()
    var onTweakGroupSelected: ((String) -> Unit)? = null

    fun setTweakGroups(tweakGroups: List<TweakGroup>) {
        this.tweakGroups.clear()
        this.tweakGroups.addAll(tweakGroups)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweakGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tweak_group, parent, false)
        return TweakGroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tweakGroups.size
    }

    override fun onBindViewHolder(holder: TweakGroupViewHolder, position: Int) {
        val tweakGroup = tweakGroups[position]
        holder.itemView.tweakGroupName.text = tweakGroup.name
        holder.itemView.setOnClickListener {
            onTweakGroupSelected?.invoke(tweakGroup.name)
        }
    }
}

internal class TweakGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)