package com.github.orkest.shazam.domain

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.github.orkest.R
import com.github.orkest.data.Song
import com.github.orkest.shazam.data.AudioChunk
import com.shazam.shazamkit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                if (!::catalog.isInitialized)
                    createCatalog(context)

                //If the streamingSession is not initialized, create it:
                if (!::currentSession.isInitialized)
                    currentSession = (ShazamKit.createStreamingSession(
                        catalog,
                        AudioSampleRateInHz.SAMPLE_RATE_48000,
                        ShazamConstants.SHAZAM_SESSION_READ_BUFFER_SIZE
                    ) as ShazamKitResult.Success).data

                // records audio and flows it to the StreamingSession for recognition
                coroutineScope.launch {
                    recording.collect { audioChunk ->
                        currentSession.matchStream(
                            audioChunk.buffer,
                            audioChunk.meaningfulLengthInBytes,
                            audioChunk.timestamp
                        )
                    }
                }

                // collect the results
                coroutineScope.launch {

                    Log.d("AudioRecognition", "waiting for results")
                    currentSession.recognitionResults().collect { matchResult ->
                        AudioRecording.stopRecording(coroutineScope)
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
            return song
        }

        /**
         * Creates a signature for the given song (available in the resources)
         */
        fun createSignature(bytes: ByteArray, context: Context, coroutineScope: CoroutineScope): CompletableFuture<MatchResult> {

            val future = CompletableFuture<MatchResult>()

            coroutineScope.launch {
                val signatureGenerator =
                    (ShazamKit.createSignatureGenerator(AudioSampleRateInHz.SAMPLE_RATE_48000)
                            as ShazamKitResult.Success).data

                // val bytes = readAudioFileToByteArray(context, song)
                Log.d("AudioRecognition", "Bytes size: ${bytes.size}")
                Log.d("AudioRecognition", "Bytes: ${bytes.contentToString()}")

                signatureGenerator.append(bytes, bytes.size, System.currentTimeMillis())
                val signature = signatureGenerator.generateSignature()
                Log.d("AudioRecognition", "Signature: $signature")

            }
            return future
        }
    }
}
