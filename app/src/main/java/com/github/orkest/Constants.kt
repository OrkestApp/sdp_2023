package com.github.orkest

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
    }
}