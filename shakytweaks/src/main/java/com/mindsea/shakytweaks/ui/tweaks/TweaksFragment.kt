package com.mindsea.shakytweaks.ui.tweaks


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration

import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ShakyTweaks
import kotlinx.android.synthetic.main.fragment_tweaks.*
import java.lang.IllegalStateException

private const val TWEAK_GROUP_ARG_PARAM = "TweaksFragment.tweak_group"

internal class TweaksFragment : Fragment() {

    private lateinit var viewModel: TweaksViewModel
    private lateinit var adapter: TweaksAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tweaks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tweakGroupId = arguments?.getString(TWEAK_GROUP_ARG_PARAM) ?: throw IllegalStateException("tweak group must be present")
        setupRecyclerView()
        val libraryModule = ShakyTweaks.module()
        viewModel = TweaksViewModel(libraryModule.tweakProvider(), libraryModule.tweakValueResolver(), tweakGroupId)
        viewModel.onUpdate = this::updateView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onUpdate = null
    }

    private fun updateView(state: TweaksState) {
        adapter.setTweaks(state.tweaks)
    }

    private fun setupRecyclerView() {
        adapter = TweaksAdapter()
        tweaksRecyclerView.adapter = adapter
        tweaksRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

}

internal fun createTweaksFragment(tweakGroupId: String): TweaksFragment {
    val fragment = TweaksFragment()
    fragment.arguments = Bundle().apply {
        putString(TWEAK_GROUP_ARG_PARAM, tweakGroupId)
    }
    return fragment
}
