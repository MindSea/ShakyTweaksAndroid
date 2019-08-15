package com.mindsea.shakytweaks.ui.tweakgroups

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.TweakProvider
import kotlinx.android.synthetic.main.fragment_tweak_groups.*

internal class TweakGroupsFragment : Fragment() {

    private var listener: TweakGroupsFragmentListener? = null
    private lateinit var viewModel: TweakGroupsViewModel
    private lateinit var adapter: TweakGroupsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tweak_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel = TweakGroupsViewModel(TweakProvider.instance)
        viewModel.onUpdate = this::updateView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onUpdate = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TweakGroupsFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement TweakGroupsFragmentListener")
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun setupRecyclerView() {
        adapter = TweakGroupsAdapter()
        adapter.onTweakGroupSelected = { groupId ->
            listener?.onGroupSelected(groupId)
        }
        tweakGroupsRecyclerView.adapter = adapter
        tweakGroupsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun updateView(state: TweakGroupsState) {
        adapter.setTweakGroups(state.groups)
    }

    interface TweakGroupsFragmentListener {
        fun onGroupSelected(groupId: String)
    }
}