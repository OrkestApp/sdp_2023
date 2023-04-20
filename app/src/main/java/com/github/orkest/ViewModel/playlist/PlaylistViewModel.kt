package com.github.orkest.ViewModel.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.github.orkest.Model.Song
import java.util.concurrent.CompletableFuture

class PlaylistViewModel : ViewModel() {

    private val dbAPI = FireStoreDatabaseAPI()

    // List of songs in the playlist
    private var songs : List<Song> = listOf()

    // method to fetch songs from database
    fun fetchSongs(sender: String, receiver: String) : CompletableFuture<List<Song>>  {
        Log.d("DEBUG FETCH", "FETCHING2")
        return dbAPI.fetchSharedSongsFromDataBase(receiver, sender)
    }

    fun storeSong(song: Song, sender: String, receiver: String) {
        songs = songs.plus(song)
        dbAPI.storeSharedSongToDataBase(song, sender, receiver)
    }


}