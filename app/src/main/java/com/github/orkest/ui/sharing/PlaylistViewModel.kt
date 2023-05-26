package com.github.orkest.ui.sharing

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.orkest.data.Song
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.domain.persistence.AppDao
import com.github.orkest.domain.persistence.AppEntities
import java.util.concurrent.CompletableFuture

/*
    * ViewModel for the PlaylistActivity
*/
open class PlaylistViewModel(private val songDao: AppDao.Companion.SongDao) : ViewModel() {

    private val dbAPI = FireStoreDatabaseAPI()

    // List of songs in the playlist
    private var songs : List<Song> = listOf()

    // method to fetch songs from database
    fun fetchSongs(sender: String, receiver: String, context: Context) : CompletableFuture<List<Song>>  {
        // check internet connection
        return if (FireStoreDatabaseAPI.isOnline(context)){
            dbAPI.fetchSharedSongsFromDataBase(receiver, sender)
        }else{
            var songList: List<Song> = listOf()
            // get songs from cache
            CompletableFuture.runAsync {
                songList = this.songDao.getAllSongs().map {song ->
                    Song(song.name, song.Artist, song.Album, song.URL)
                }.plus(songs)
            }

            CompletableFuture.completedFuture(songList)
        }
    }



    // method to store songs in database
    fun storeSong(song: Song, sender: String, receiver: String, context: Context) {
        CompletableFuture.runAsync {
            if (FireStoreDatabaseAPI.isOnline(context)){
                // cache the new song in the database
                this.songDao.insertAll(AppEntities.Companion.SongEntity(
                    // random id
                    id = (0..100000).random(),
                    name = song.Title,
                    Artist = song.Artist,
                    Album = song.Album,
                    URL = song.URL
                ))

                songs = songs.plus(song)
                dbAPI.storeSharedSongToDataBase(song, sender, receiver)
            }
            // TODO: notify user that song cannot be shared offline
        }
    }


}