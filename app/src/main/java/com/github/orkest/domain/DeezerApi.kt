package com.github.orkest.domain

import java.net.URL

interface DeezerApi : HttpHandler{

    fun searchAsong(songName : String, artistName:String) : String{
        val url = URL("https://api.deezer.com/search/track?q=$songName $artistName")
         return PostOrGEtAndGetResponse(url,false)
    }

    fun fetchTheUserId(access_token:String): String{
        return PostOrGEtAndGetResponse(URL("https://api.deezer.com/user/me?access_token=$access_token"),false)
    }

    fun createANewPlaylistOnTheUserProfile(userId:String,access_token: String,playlistTitle: String="Orkest") :String{
        return PostOrGEtAndGetResponse(URL("https://api.deezer.com/user/$userId/playlists?access_token=$access_token&title=$playlistTitle"),true)
    }

    fun addANewSongToOrkestPlayList(access_token: String,playlistId: String="Orkest",trackId:String):String{
        return PostOrGEtAndGetResponse(URL("https://api.deezer.com/playlist/$playlistId/tracks?access_token=$access_token&songs=$trackId"),true)
    }

}

class deezerApiImplemented : DeezerApi{

}