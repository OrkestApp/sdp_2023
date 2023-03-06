package com.github.orkest.DataModel

/**
 * Data class representing attributes of the user displayed in his profile
 *
 * username (String) : Unique identifier of the user associated to this profile
 * profilePictureId (Int) : Key of the profile picture of this profile
 * bio (String) : Description of this profile
 * nbFollowers (Int) : Number of users following the user associated to this profile
 * nbFollowing (Int) : Number of users followed by the user associated to this profile
 * favoriteSongs (List<Song>) : Favorite songs shared by the user of this profile
 * sharedMusic (List<Song>) : The list of all songs shared publicly by the user of this profile
 *
 */
data class Profile (var username: String,
                    var profilePictureId: Int,
                    var bio: String,
                    var nbFollowers: Int,
                    var nbFollowing: Int,
                    var favoriteSongs: List<Song>,
                    var sharedMusic: List<Song>)