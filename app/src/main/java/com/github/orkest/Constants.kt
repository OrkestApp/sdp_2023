package com.github.orkest

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.github.orkest.Model.PlaySpotify
import com.github.orkest.Model.Providers
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

        var CURRENT_USER_PROVIDER: Providers = Providers.SPOTIFY
        private var currentLoggedUser: String = ""
        var CURRENT_LOGGED_USER: String
            get() = currentLoggedUser
            set(value) { currentLoggedUser = value }

        val DB_ZONE_ID: ZoneId = ZoneId.of("Europe/Paris")

        val FONT_MARKER = FontFamily(
            Font(R.font.permanentmarker_regular, FontWeight.Normal)
        )

        val DUMMY_RUDE_BOY_SONG = Song("Rude Boy", "Rihanna", "Rated R",
            "link")

        val DUMMY_LAST_CONNECTED_TIME: LocalDateTime =
            LocalDateTime.of(2021, 5, 1, 12, 0)



        /**
         * Function to play a song on the user's provider app
         */
        fun playMusicButtonClicked(song: Song, isPlayed: MutableState<Boolean>, context: Context) {
            if (Constants.CURRENT_USER_PROVIDER == Providers.SPOTIFY) {
                PlaySpotify.play(song)
                isPlayed.value = !isPlayed.value
            }

            //TODO: Uncomment this when the deezer integration is merged

//            if (Constants.CURRENT_USER_PROVIDER == Providers.DEEZER) {
//                DeezerApiIntegration.launchDeezerToPlaySong(song.name, song.artist).whenComplete { intent, _ ->
//                    context.startActivity(intent)
//                    isPlayed.value = !isPlayed.value
//                }
//            }
        }


        val NOTIFICATION_CHANNEL_ID = "your_channel_id"
        val MOCK_USER = User("Jacob", profile = Profile(username = "Jacob", profilePictureId = R.drawable.powerrangerblue))

    }
}