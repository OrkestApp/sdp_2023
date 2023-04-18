package com.github.orkest.Model

import android.Manifest
import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import com.github.orkest.Constants
import com.shazam.shazamkit.AudioSampleRateInHz
import com.shazam.shazamkit.MatchResult
import com.shazam.shazamkit.ShazamKit
import com.shazam.shazamkit.ShazamKitResult
import com.shazam.shazamkit.SignatureGenerator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture

class AudioRecognition {

    companion object {

         val catalog =
                ShazamKit.createCustomCatalog().apply {

                }

        private fun Int.toByteAllocation(): Int {
            return when (this) {
                AudioFormat.ENCODING_PCM_16BIT -> 2
                else -> throw IllegalArgumentException("Unsupported encoding")
            }
        }

        private fun ByteBuffer.putTrimming(byteArray: ByteArray) {
            if (byteArray.size <= this.capacity() - this.position()) {
                this.put(byteArray)
            } else {
                this.put(byteArray, 0, this.capacity() - this.position())
            }
        }

        /**
         * Records audio for the maximum query duration and returns an [AudioChunk] with the recorded audio
         * @return [AudioChunk] with the recorded audio
         * @throws [SecurityException] if the app does not have the RECORD_AUDIO permission
         */
        @RequiresPermission(Manifest.permission.RECORD_AUDIO)
        private fun audioChunkRecord(): AudioChunk {
            val audioSource = MediaRecorder.AudioSource.UNPROCESSED

            val audioFormat = AudioFormat.Builder()
                .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(48_000)
                .build()

            val audioRecord = AudioRecord.Builder()
                .setAudioSource(audioSource)
                .setAudioFormat(audioFormat)
                .build()

            val seconds = catalog.maximumQuerySignatureDurationInMs

            // Final desired buffer size to allocate necessary seconds of audio
            val size = audioFormat.sampleRate * audioFormat.encoding.toByteAllocation() * seconds
            val destination = ByteBuffer.allocate(size.toInt())

            // Small buffer to retrieve chunks of audio
            val bufferSize = AudioRecord.getMinBufferSize(
                48_000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            audioRecord.startRecording()
            val readBuffer = ByteArray(bufferSize)
            while (destination.remaining()>0) {
                val actualRead = audioRecord.read(readBuffer, 0, bufferSize)
                val byteArray = readBuffer.sliceArray(0 until actualRead)
                destination.putTrimming(byteArray)
            }
            audioRecord.release()

            return AudioChunk(destination.array(), destination.position(), System.currentTimeMillis())
        }

        //TODO: Check if this is a correct way to do dependency injection
        /**
         * Records AudioChunks continuously and returns a [Flow] of [AudioChunk]s
         * Must be called in the coroutineScope as the streaming session
         */
        @RequiresPermission(Manifest.permission.RECORD_AUDIO)
        fun recordingFlow(coroutineScope: CoroutineScope): Flow<AudioChunk> = flow {
            while (currentCoroutineContext().isActive) {
                emit(audioChunkRecord())
                delay(100)
            }
        }


        //TODO: add dependency injection for the recordingFlow
        @RequiresPermission(Manifest.permission.RECORD_AUDIO)
        suspend fun recognizeSong(recordingFlow: Flow<AudioChunk>,
                                  coroutineScope: CoroutineScope): CompletableFuture<String>{

            val currentSession = (ShazamKit.createStreamingSession(
                catalog,
                AudioSampleRateInHz.SAMPLE_RATE_48000,
                Constants.SHAZAM_SESSION_READ_BUFFER_SIZE
            ) as ShazamKitResult.Success).data


            // val coroutineScope = CoroutineScope(Dispatchers.Default)

            // records audio and flows it to the StreamingSession
            coroutineScope.launch {
                recordingFlow.collect { audioChunk ->
                    currentSession.matchStream(
                        audioChunk.buffer,
                        audioChunk.meaningfulLengthInBytes,
                        audioChunk.timestamp
                    )
                }
            }

            val title = CompletableFuture<String>()
            // collect the results
            coroutineScope.launch {
                currentSession.recognitionResults().collect { matchResult ->
                    if (matchResult is MatchResult.Match) {
                        title.complete(matchResult.matchedMediaItems[0].title!!)
                    }
                    if (matchResult is MatchResult.NoMatch) {
                        title.complete("No match found")
                    }

                    if (matchResult is MatchResult.Error) {
                        title.completeExceptionally(matchResult.exception)
                    }
                }
            }
            return title
        }
    }

    data class AudioChunk (
        val buffer: ByteArray = ByteArray(Constants.SHAZAM_SESSION_READ_BUFFER_SIZE),
        val meaningfulLengthInBytes: Int = Constants.SHAZAM_SESSION_READ_BUFFER_SIZE,
        val timestamp: Long = System.currentTimeMillis()
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AudioChunk

            if (!buffer.contentEquals(other.buffer)) return false
            if (meaningfulLengthInBytes != other.meaningfulLengthInBytes) return false
            if (timestamp != other.timestamp) return false

            return true
        }

        override fun hashCode(): Int {
            var result = buffer.contentHashCode()
            result = 31 * result + meaningfulLengthInBytes
            result = 31 * result + timestamp.hashCode()
            return result
        }
    }
}


