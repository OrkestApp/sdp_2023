package com.github.orkest.shazam.data

import com.github.orkest.shazam.domain.ShazamConstants

data class AudioChunk (
    val buffer: ByteArray = ByteArray(ShazamConstants.SHAZAM_SESSION_READ_BUFFER_SIZE),
    val meaningfulLengthInBytes: Int = ShazamConstants.SHAZAM_SESSION_READ_BUFFER_SIZE,
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