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


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration

import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ShakyTweaks
import com.mindsea.shakytweaks.databinding.FragmentTweaksBinding
import java.lang.IllegalStateException

private const val TWEAK_GROUP_ARG_PARAM = "TweaksFragment.tweak_group"

internal class TweaksFragment : Fragment() {

    private lateinit var binding: FragmentTweaksBinding
    private lateinit var viewModel: TweaksViewModel
    private lateinit var adapter: TweaksAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTweaksBinding.inflate(inflater)
        return binding.root
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
        binding.tweaksRecyclerView.adapter = adapter
        binding.tweaksRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

}

internal fun createTweaksFragment(tweakGroupId: String): TweaksFragment {
    val fragment = TweaksFragment()
    fragment.arguments = Bundle().apply {
        putString(TWEAK_GROUP_ARG_PARAM, tweakGroupId)
    }
    return fragment
}
