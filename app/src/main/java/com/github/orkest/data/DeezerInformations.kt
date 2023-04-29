package com.github.orkest.data

/**
 * This class is meant to store all the informations Orkest needs to operate with Deezer
 * @access_token is the non expiring token that is allow to do API command for the user
 * @userId is the unique identifier of the user Deezer Account
 * @playlistId is the unique identifier of the playlist that orkest will update
 */
data class DeezerInformations(var access_token :String, var userId :String, var playlistId:String)