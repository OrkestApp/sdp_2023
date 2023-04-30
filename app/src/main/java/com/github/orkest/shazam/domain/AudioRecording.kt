package com.github.orkest.shazam.domain

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.github.orkest.shazam.data.AudioChunk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AudioRecording {

    companion object {

       @RequiresPermission(Manifest.permission.RECORD_AUDIO)
       private fun buildAudioRecord(): AudioRecord {
           //Set the audio source
           val audioSource = MediaRecorder.AudioSource.UNPROCESSED

           //Set the audio format
           val audioFormat = AudioFormat.Builder()
               .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
               .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
               .setSampleRate((48_000))
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
                ShazamConstants.RECORDING_SAMPLE_RATE,
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
                    delay(10)
                }
                //Stop the audioRecord when the coroutine is cancelled
                // (either because of result, or user stopped recording)
                audioRecord.release()
            }
        }

        /**
         * Collects recorded AudioChunks continuously and logs them
         * @param coroutineScope the same coroutineScope as the streaming session
         * @throws [SecurityException] if the app does not have the RECORD_AUDIO permission
         */
        @RequiresPermission(Manifest.permission.RECORD_AUDIO)
        fun logsRecordedAudio(coroutineScope: CoroutineScope){
            coroutineScope.launch {
                recordingFlow(coroutineScope).collect {
                    Log.d("HasOnesBuffer", it.buffer.sum().toString())
                    Log.d("LengthBuffer", it.buffer.size.toString())
                    Log.d("ShazamActivity", it.timestamp.toString())
                }
            }

        }

        /**
         * Stops the collection of the recorded AudioChunks
         * Must be called in the same coroutineScope as logsRecordedAudio
         * @param coroutineScope the same coroutineScope as logsRecordedAudio
         */
        fun stopRecording(coroutineScope: CoroutineScope){
            coroutineScope.cancel()
        }
    }
}


