package com.github.orkest

import java.time.ZoneId

/**
 * Class to store the current logged-in username since it is needed in test classes, composable functions and viewModels
 */
class Constants {
    companion object{
        private var _currentLoggedUser: String = ""
        var currentLoggedUser: String
            get() = _currentLoggedUser
            set(value) { _currentLoggedUser = value }

        val DB_ZONE_ID = ZoneId.of("Europe/Paris")
    }
}