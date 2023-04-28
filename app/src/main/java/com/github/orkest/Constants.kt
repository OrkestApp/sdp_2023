package com.github.orkest

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.github.orkest.Model.Profile
import com.github.orkest.Model.Song
import com.github.orkest.Model.User
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Class to store the current logged-in username since it is needed in test classes, composable functions and viewModels
 */
class Constants {
    companion object{
        var AudioPermissionGranted: Boolean = false
        const val RECORDING_SAMPLE_RATE: Int  = 48_000
        const val SHAZAM_SESSION_READ_BUFFER_SIZE: Int = 4096

        private var currentLoggedUser: String = ""
        var CURRENT_LOGGED_USER: String
            get() = currentLoggedUser
            set(value) { currentLoggedUser = value }

        val DB_ZONE_ID: ZoneId = ZoneId.of("Europe/Paris")

        val FONT_MARKER = FontFamily(
            Font(R.font.permanentmarker_regular, FontWeight.Normal)
        )

        val DUMMY_RUDE_BOY_SONG = Song("Rude Boy", "Rihanna", "Rated R",
            "link", R.drawable.album_cover)

        val DUMMY_LAST_CONNECTED_TIME: LocalDateTime =
            LocalDateTime.of(2021, 5, 1, 12, 0)

        val MOCK_USER = User("Jacob", profile = Profile(username = "Jacob", profilePictureId = R.drawable.powerrangerblue))

    }
}