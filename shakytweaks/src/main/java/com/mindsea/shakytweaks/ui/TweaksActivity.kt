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

package com.mindsea.shakytweaks.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ui.TweakActivitySection.*
import com.mindsea.shakytweaks.ui.tweakgroups.TweakGroupsFragment
import com.mindsea.shakytweaks.ui.tweaks.createTweaksFragment

internal class TweaksActivity : AppCompatActivity(),
    TweakGroupsFragment.TweakGroupsFragmentListener {

    private lateinit var viewModel: TweaksActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweaks)
        viewModel = TweaksActivityViewModel()
        supportActionBar?.let {
            it.title = resources.getString(R.string.tweaks_title)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.onUpdate = this::updateView
    }

    override fun onDestroy() {
        viewModel.onUpdate = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.backPressed()
        return true
    }


    override fun onBackPressed() {
        viewModel.backPressed()
    }

    override fun onGroupSelected(groupId: String) {
        viewModel.groupSelected(groupId)
    }

    private fun updateView(state: TweaksActivityState) {
        when (val currentSection = state.currentSection) {
            is None -> finish()
            is Groups -> {
                supportActionBar?.let {
                    it.title = resources.getString(R.string.tweaks_title)
                }
                if (state.previousSection is None) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, TweakGroupsFragment())
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                        .replace(R.id.container, TweakGroupsFragment())
                        .commit()
                }
            }
            is Tweaks -> {
                supportActionBar?.title = currentSection.groupId
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                    .replace(R.id.container, createTweaksFragment(currentSection.groupId))
                    .commit()
            }
        }
    }
}

internal fun createTweaksActivityIntent(context: Context) =
    Intent(context, TweaksActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }