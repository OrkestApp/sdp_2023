package com.github.emilehreich.bootcamp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ApiActivityTest {

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(ApiActivity::class.java)


}
