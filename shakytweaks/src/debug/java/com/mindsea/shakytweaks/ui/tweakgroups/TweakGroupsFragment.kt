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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ShakyTweaks
import kotlinx.android.synthetic.debug.fragment_tweak_groups.*

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
        viewModel = TweakGroupsViewModel(ShakyTweaks.module().tweakProvider())
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