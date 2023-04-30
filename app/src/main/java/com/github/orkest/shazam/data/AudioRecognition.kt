package com.github.orkest.shazam.data

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.github.orkest.R
import com.github.orkest.data.Song
import com.github.orkest.shazam.domain.AudioChunk
import com.github.orkest.shazam.domain.ShazamConstants
import com.shazam.shazamkit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

/**
 * This class is responsible for the audio recognition process using ShazamKit
 */
class AudioRecognition {


    companion object {

        // create a custom catalog for the recognition mechanism
        private lateinit var catalog : Catalog
        private lateinit var currentSession: StreamingSession


        /**
         * Recognizes the song from the audio stream
         * @param coroutineScope the coroutine scope
         * @param context the context
         * @param recording the audio stream
         * @return the song
         */
        @RequiresPermission(Manifest.permission.RECORD_AUDIO)
        fun recognizeSong(coroutineScope: CoroutineScope, context: Context, recording: Flow<AudioChunk>): CompletableFuture<Song> {
            val song = CompletableFuture<Song>()
            coroutineScope.launch {

                //If catalog is not initialized, create it:
                if (!Companion::catalog.isInitialized)
                    createCatalog(context)

                //If the streamingSession is not initialized, create it:
                if (!Companion::currentSession.isInitialized)
                    currentSession = initStreamingSession(context)

                // records audio and flows it to the StreamingSession for recognition
                coroutineScope.launch {
                    recording.collect { audioChunk ->
                       recognizeAudioChunk(audioChunk)
                    }
                }

                // collect the results
                coroutineScope.launch {
                    Log.d("AudioRecognition", "waiting for results")
                    currentSession.recognitionResults().collect { matchResult ->
                        collectMatchResult(matchResult, song)
                    }
                }
            }
            return song
        }

        /**
         * Initializes the catalog to compare the audio stream with, already created with shazam CLI
         * @param context the context from which the catalog is created
         */
        private suspend fun createCatalog(context: Context) {
            catalog = ShazamKit.createCustomCatalog()
                .apply {
                    // add the audio file to the catalog
                    //Do pattern matching on the audio file (success or failure)
                    when (val audio = addFromCatalog(context.resources.openRawResource(R.raw.orkest))) {
                        is ShazamKitResult.Success -> Log.d("AudioRecognition", audio.toString())
                        is ShazamKitResult.Failure -> Log.d("AudioRecognition", audio.reason.toString())
                    }
                }
        }

        /**
         * Flows audioChunks to the streamingSession to be recognized by the ShazamKit
         * @param audioChunk the audioChunk to be recognized
         */
        private fun recognizeAudioChunk(audioChunk: AudioChunk){
            currentSession.matchStream(
                audioChunk.buffer,
                audioChunk.meaningfulLengthInBytes,
                audioChunk.timestamp
            )
        }

        /**
         * Initializes the streaming session
         * @param context the context from which the streaming session is created
         */
        private suspend fun initStreamingSession(context: Context): StreamingSession {
            return (ShazamKit.createStreamingSession(
                catalog,
                AudioSampleRateInHz.SAMPLE_RATE_48000,
                ShazamConstants.SHAZAM_SESSION_READ_BUFFER_SIZE
            ) as ShazamKitResult.Success).data
        }

        /**
         * Collects and handle the match result response
         * @param matchResult the match result from the streaming session
         * @param song the song to be completed based on the value [matchResult] received
         */
        private fun collectMatchResult(matchResult: MatchResult, song: CompletableFuture<Song>) {
            if (matchResult is MatchResult.Match) {
                val title = matchResult.matchedMediaItems[0].title!!
                val artist = matchResult.matchedMediaItems[0].artist!!
                song.complete(Song(title, artist))
            }
            if (matchResult is MatchResult.NoMatch) {
                song.complete(ShazamConstants.SONG_NO_MATCH)
            }

            if (matchResult is MatchResult.Error) {
                song.completeExceptionally(matchResult.exception)
            }
            Log.d("AudioRecognition", "Match result: $matchResult")

        }
    }
}
