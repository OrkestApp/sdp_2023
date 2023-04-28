package com.github.orkest.Model

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.orkest.Constants
import com.shazam.shazamkit.ShazamKit
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AudioRecognition {

    companion object {

        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

         val catalog =
                ShazamKit.createCustomCatalog().apply {

                }

        /**
         * Asks for the audio permission if it is not granted yet
         * @param context the context of the application
         * @param activity the activity that is asking for the permission
         */
        private fun askAudioPermission(context: Context, activity: Activity) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO) ,
                    REQUEST_RECORD_AUDIO_PERMISSION);
            }
            else {
                Log.d("AudioRecognition", "Permission already granted")
                Constants.AudioPermissionGranted = true
            }
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

            //Return the flow of AudioChunks, will be actually run in a suspending way when collected
            return flow {
                audioRecord.startRecording()
                //Collect audio chunks while the coroutine is active
                while (currentCoroutineContext().isActive) {
                    val actualRead = audioRecord.read(readBuffer, 0, readBuffer.size)
                    val byteArray = readBuffer.sliceArray(0 until actualRead)
                    val audioChunk = AudioChunk(byteArray, actualRead, System.currentTimeMillis())
                    emit(audioChunk)
                }
                //Stop the audioRecord when the coroutine is cancelled
                // (either because of result, or user stopped recording)
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


