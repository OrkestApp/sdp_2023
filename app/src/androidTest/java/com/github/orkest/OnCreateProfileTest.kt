package com.github.orkest

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.orkest.ui.EditProfileActivity
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MockEditProfileV2 {

    @Test
    fun onCreate() {
        val scenario = ActivityScenario.launch(EditProfileActivity::class.java)
        scenario.onActivity { activity ->
            assertNotNull(activity)
        }
    }
}