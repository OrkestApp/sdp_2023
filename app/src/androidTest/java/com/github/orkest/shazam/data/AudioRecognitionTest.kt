package com.github.orkest.shazam.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.github.orkest.shazam.domain.AudioChunk
import com.github.orkest.shazam.domain.ShazamConstants
import com.shazam.shazamkit.AudioSampleRateInHz
import com.shazam.shazamkit.MatchResult
import com.shazam.shazamkit.ShazamKit
import com.shazam.shazamkit.ShazamKitResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.http.GET
import java.util.concurrent.CompletableFuture

class AudioRecognitionTest {

    lateinit var context: Context
    lateinit var coroutineScope: CoroutineScope

    @get:Rule
   val composeTestRule = createComposeRule()

    @Before
    fun setup(){
        context = ApplicationProvider.getApplicationContext<Application>()
        composeTestRule.setContent {
            coroutineScope = rememberCoroutineScope()
        }
    }


    @Test
    fun sameSongIsRecognized() {
        val bytes = context.resources.getIdentifier("booba_petite_fille", "raw", context.packageName)
            .let { context.resources.openRawResource(it).readBytes() }
        //we create a flow of audio chunks with the bytes of the song
        //Each audioChunk contains 4096 bytes 
        val audioChunks = bytes.chunked(4096).map { AudioChunk(it) }

        val recording = audioChunks.asFlow()

        AudioRecognition.recognizeSong(coroutineScope,context,recording).thenAccept {
            assertEquals("Booba", it.Artist)
            assertEquals("Petite Fille", it.Title)
        }
    }

    @Test
    fun zeroValueBytesNotRecognized(){
        val bytes = ByteArray(4*4096) { 0 }
        val audioChunks = bytes.chunked(4096).map { AudioChunk(it) }.asFlow()

        AudioRecognition.recognizeSong(coroutineScope,context,audioChunks).thenAccept {
            assertEquals(it, ShazamConstants.SONG_NO_MATCH)
        }
    }

    @Test
    fun noAudioChunksGenError() {
        val audioChunks = emptyList<AudioChunk>().asFlow()

         AudioRecognition.recognizeSong(coroutineScope,context,audioChunks).thenAccept {
            assertEquals(it, null)
        }
    }
}

/**
 * Splits the ByteArray into chunks of size i
 */
private fun ByteArray.chunked(i: Int): List<ByteArray> {
    val result = mutableListOf<ByteArray>()
    var startIndex = 0
    while (startIndex < this.size) {
        val endIndex = minOf(startIndex + size, this.size)
        result.add(copyOfRange(startIndex, endIndex))
        startIndex = endIndex
    }
    return result
}
