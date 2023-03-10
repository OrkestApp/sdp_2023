package com.github.orkest.DataModel

/**
 * Data class representing a user and its various attributes
 *
 * username (String) : unique identifier of this user
 * mail (String) : The mail of this user
 * profile (Profile) : The profile of this user displayed to other users
 * sharedWithMe (List<Song> : List of the songs shared with me
 * followers (List<String>) : List of the usernames of the users following this instance
 * following (List<String>) : List of the usernames of the users followed by this instance
 */
data class User(var username: String = "",
                var mail: String ="",
                var serviceProvider: String ="",
                var profile: Profile? = null,
                var sharedWithMe : List<Song> = ArrayList(),
                var followers: List<String> = ArrayList(),
                var following : List<String> = ArrayList())