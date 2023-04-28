package com.github.orkest.Model

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.rule.GrantPermissionRule
import kotlinx.coroutines.*
import okhttp3.internal.wait
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AudioRecognitionTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var coroutine : CoroutineScope

    @get:Rule
    var permissionAudio = GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    fun setUp() {
        composeTestRule.setContent {
            coroutine = rememberCoroutineScope()
        }
    }


    @Test
    fun audioIsRecorded() {
        val audio = AudioRecognition.recordingFlow(coroutine)
        var counter = 0
        println("Starting test")
        coroutine.launch {
                audio.collect{
                    ++counter
                    coroutine.cancel()
                }
        }
        assertTrue(counter > 0)
    }


}