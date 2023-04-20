package com.github.orkest

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.github.orkest.Model.Song
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Class to store the current logged-in username since it is needed in test classes, composable functions and viewModels
 */
class Constants {
    companion object{
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

        val NOTIFICATION_CHANNEL_ID = "your_channel_id"


    }
}