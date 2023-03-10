package com.github.orkest.Model

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
data class User(var username: String,
                var mail: String,
                var profile: Profile,
                var sharedWithMe : List<Song>,
                var followers: List<String>,
                var following : List<String>)
