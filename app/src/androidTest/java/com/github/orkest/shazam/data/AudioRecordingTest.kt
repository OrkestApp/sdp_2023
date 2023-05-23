package com.github.orkest.shazam.data

import androidx.test.rule.GrantPermissionRule
import com.github.orkest.shazam.domain.AudioChunk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


class AudioRecordingTest {

    @get:Rule
    var permissionAudio: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Test
    fun testGetAudioRecording() {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val result: Flow<AudioChunk> = AudioRecording.recordingFlow()

        val listAudioChunk = mutableListOf<AudioChunk>()

        coroutineScope.launch {
            result.collect { audioChunk ->
                listAudioChunk.add(audioChunk)
                AudioRecording.stopRecording(this)

            }
        }

        Thread.sleep(1000)
        assert(listAudioChunk.size > 0)
    }




}