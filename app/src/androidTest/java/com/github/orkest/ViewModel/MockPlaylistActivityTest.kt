package com.github.orkest.ViewModel

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.orkest.ui.sharing.PlaylistActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MockPlaylistActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<PlaylistActivity> = ActivityScenarioRule(PlaylistActivity::class.java)

    @Test
    fun activityLaunches() {
        // Activity is launched
    }
}