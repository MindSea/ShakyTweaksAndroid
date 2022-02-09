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
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ShakyTweaks
import com.mindsea.shakytweaks.databinding.FragmentTweakGroupsBinding

internal class TweakGroupsFragment : Fragment() {

    private var listener: TweakGroupsFragmentListener? = null
    private lateinit var binding: FragmentTweakGroupsBinding
    private lateinit var viewModel: TweakGroupsViewModel
    private lateinit var adapter: TweakGroupsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentTweakGroupsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val libraryModule = ShakyTweaks.module()
        viewModel =
            TweakGroupsViewModel(libraryModule.tweakProvider(), libraryModule.tweakValueResolver())
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tweaks_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_reset_tweaks -> {
                displayResetTweaksConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        adapter = TweakGroupsAdapter()
        adapter.onTweakGroupSelected = { groupId ->
            listener?.onGroupSelected(groupId)
        }
        binding.tweakGroupsRecyclerView.adapter = adapter
        binding.tweakGroupsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun updateView(state: TweakGroupsState) {
        adapter.setTweakGroups(state.groups)
    }


    private fun displayResetTweaksConfirmationDialog() = context?.let {
        AlertDialog.Builder(it)
            .setTitle(R.string.dialog_title)
            .setPositiveButton(R.string.dialog_confirm_action) { _, _ ->
                viewModel.resetTweaks()
            }
            .setNegativeButton(R.string.dialog_cancel_action) { _, _ ->
                // Do nothing
            }
            .create()
            .show()
    }

    interface TweakGroupsFragmentListener {
        fun onGroupSelected(groupId: String)
    }
}