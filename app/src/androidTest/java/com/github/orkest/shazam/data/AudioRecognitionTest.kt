package com.github.orkest.shazam.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.github.orkest.shazam.domain.AudioChunk
import com.github.orkest.shazam.domain.ShazamConstants
import com.shazam.shazamkit.AudioSampleRateInHz
import com.shazam.shazamkit.MatchResult
import com.shazam.shazamkit.ShazamKit
import com.shazam.shazamkit.ShazamKitResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.http.GET
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread

class AudioRecognitionTest {

    lateinit var context: Context
    lateinit var coroutineScope: CoroutineScope

    @get:Rule
   val composeTestRule = createComposeRule()

    //add recording permissions
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    fun setup(){
        context = ApplicationProvider.getApplicationContext<Application>()
        composeTestRule.setContent {
            coroutineScope = rememberCoroutineScope()
        }
    }


    @Test
    fun sameSongIsRecognized() {
        val bytes = context.resources.getIdentifier("hips_dont_lie", "raw", context.packageName)
            .let { context.resources.openRawResource(it).readBytes() }
        //we create a flow of audio chunks with the bytes of the song
        //Each audioChunk contains 4096 bytes 
        val recording = bytes.chunked(2280).asFlow()

        AudioRecognition.recognizeSong(coroutineScope,context,recording).thenAccept {
            assertEquals("Shakira", it.Artist)
            assertEquals("Hips Don't Lie", it.Title)
        }

        Thread.sleep(3000)

    }

    @Test
    fun zeroValueBytesNotRecognized(){
        val bytes = ByteArray(1000*2280) { 0 }
        val audioChunks = bytes.chunked(2280).asFlow()

        AudioRecognition.recognizeSong(coroutineScope,context,audioChunks).thenAccept {
            assertEquals(it, ShazamConstants.SONG_NO_MATCH)
        }

        Thread.sleep(3000)

    }



//    @Test
//    fun noAudioChunksGenError() {
//        val audioChunks = emptyList<AudioChunk>().asFlow()
//
//         AudioRecognition.recognizeSong(coroutineScope,context,audioChunks).thenAccept {
//            assertEquals(it, null)
//        }.get()
//    }
}

/**
 * Splits the ByteArray into chunks of size i
 */
private fun ByteArray.chunked(i: Int): List<AudioChunk> {
    val result = mutableListOf<AudioChunk>()
    var startIndex = 0
    while (startIndex < this.size) {
        val endIndex = minOf(startIndex + i, this.size)
        result.add(AudioChunk(copyOfRange(startIndex, endIndex),
            endIndex - startIndex, startIndex.toLong()))
        startIndex = endIndex + 1
    }
    return result
}
