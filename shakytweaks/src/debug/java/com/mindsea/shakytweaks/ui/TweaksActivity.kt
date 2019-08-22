package com.mindsea.shakytweaks.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mindsea.shakytweaks.R
import com.mindsea.shakytweaks.ui.TweakActivitySection.*
import com.mindsea.shakytweaks.ui.tweakgroups.TweakGroupsFragment
import com.mindsea.shakytweaks.ui.tweaks.createTweaksFragment

internal class TweaksActivity : AppCompatActivity(), TweakGroupsFragment.TweakGroupsFragmentListener {

    private val viewModel = TweaksActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweaks)
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
                if (state.previousSection is None) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, TweakGroupsFragment())
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(0, R.anim.slide_in_right)
                        .replace(R.id.container, TweakGroupsFragment())
                        .commit()
                }

            }
            is Tweaks -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, 0)
                    .replace(R.id.container, createTweaksFragment(currentSection.groupId))
                    .commit()
            }
        }
    }
}

internal fun createTweaksActivityIntent(context: Context) = Intent(context, TweaksActivity::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
}