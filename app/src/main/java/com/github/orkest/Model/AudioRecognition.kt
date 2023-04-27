package com.github.orkest.Model

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
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

       @RequiresPermission(Manifest.permission.RECORD_AUDIO)
       private fun buildAudioRecord(): AudioRecord {
           //Set the audio source
           val audioSource = MediaRecorder.AudioSource.UNPROCESSED

           //Set the audio format
           val audioFormat = AudioFormat.Builder()
               .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
               .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
               .setSampleRate(48_000)
               .build()

           //Create and return the audio recording object based on the source and format
           return AudioRecord.Builder()
               .setAudioSource(audioSource)
               .setAudioFormat(audioFormat)
               .build()
       }


        private fun buildReadingBuffer(): ByteArray {
            // size of buffer to retrieve chunks of audio
            val bufferSize = AudioRecord.getMinBufferSize(
                Constants.RECORDING_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            // Buffer to store retrieved chunks of audio
            return ByteArray(bufferSize)
        }

        /**
         * Records AudioChunks continuously and returns a [Flow] of [AudioChunk]s
         * Must be called in the coroutineScope as the streaming session
         * @throws [SecurityException] if the app does not have the RECORD_AUDIO permission
         */
        @RequiresPermission(Manifest.permission.RECORD_AUDIO)
        fun recordingFlow(coroutineScope: CoroutineScope): Flow<AudioChunk> {
            //Build the audioRecord object
            val audioRecord = buildAudioRecord()

            //Check if the audioRecord is initialized correctly
            require(audioRecord.state == AudioRecord.STATE_INITIALIZED) {
                Log.d("AudioRecord","AudioRecord is not initialized")
            }

            //Build the buffer to store the audio chunks from the audioRecord (microphone)
            val readBuffer = buildReadingBuffer()

            //Return the flow of AudioChunks, will be actually run when collected
            return flow {
                audioRecord.startRecording()
                while (currentCoroutineContext().isActive) {
                    val actualRead = audioRecord.read(readBuffer, 0, readBuffer.size)
                    val byteArray = readBuffer.sliceArray(0 until actualRead)
                    val audioChunk = AudioChunk(byteArray, actualRead, System.currentTimeMillis())
                    emit(audioChunk)
                }
                audioRecord.release()
            }
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

            return buffer.contentEquals(other.buffer) &&
                    meaningfulLengthInBytes == other.meaningfulLengthInBytes &&
                    timestamp == other.timestamp
        }

        override fun hashCode(): Int {
            var result = buffer.contentHashCode()
            result = 31 * result + meaningfulLengthInBytes
            result = 31 * result + timestamp.hashCode()
            return result
        }
    }
}


