package com.mindsea.shakytweaks.ui.tweakgroups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mindsea.shakytweaks.R
import kotlinx.android.synthetic.main.item_tweak_group.view.*

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