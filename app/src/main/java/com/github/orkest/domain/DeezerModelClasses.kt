package com.github.orkest.domain
import kotlinx.serialization.Serializable

/**
 * We need those data classes to serialize and deserialize the data from the web API
 * Based on this doc https://developers.deezer.com/api/ but the data received are not always consistent
 */
class DeezerModelClasses {
    /**
     * This class is used to deserialize tracks from the Deezer API
     */
    @Serializable
    data class Track(val id:String,val readable: Boolean,val title:String,
                     val title_short:String, val title_version:String, val link:String,
                     val duration:String, val rank:String, val explicit_lyrics:Boolean,
                     val explicit_content_lyrics:Int, val explicit_content_cover:Int,
                     val preview:String, val md5_image:String, val artist: Artist, val album: Album, val type :String )

    /**
     * This class is used to deserialize tracks from the Deezer API
     */
    @Serializable
    data class Artist(val id:String, val name:String, val link:String, val picture:String,
                    val picture_small:String, val picture_medium:String,val picture_big:String,
                    val picture_xl:String, val tracklist:String, val type:String )

    /**
     * This class is used to deserialize tracks from the Deezer API
     */
    @Serializable
    data class Album(val id: String , val title:String, val cover :String, val cover_small:String,
                     val cover_medium:String, val cover_big:String, val cover_xl:String, val md5_image:String,
                     val tracklist:String, val type:String)


    /**
     * This class is used to deserialize playlists from the Deezer API
     */
    @Serializable
    data class Playlist(val id:String, val title: String, val public:Boolean,val nb_tracks:String,
                        val link:String, val picture :String,val picture_small: String,val picture_medium: String,
                        val picture_big: String,val picture_xl: String,val checksum:String,
                        val tracklist: String,val creation_date :String,val md5_image: String,
                        val picture_type : String,val user: User, val type: String)

    /**
     * This class is used to deserialize a user from the Deezer API
     * IMPORTANT: Only for the user object in a search for a playlist or a user ID
     */
    @Serializable
    data class User(val id:String, val name:String, val tracklist: String,val type:String)

    @Serializable
    data class UserExtended(val id:String, val name:String, val lastname:String,val firstname:String,
                            val email:String, val status:String, val birthday:String, val inscription_date:String,
                            val gender:String, val link:String, val picture :String, val picture_small:String,
                            val picture_medium:String, val picture_big:String, val picture_xl:String,val country:String,
                            val lang:String,val is_kid:String, val explicit_content_level:String,val explicit_content_levels_available: List<String>,
                            val tracklist:String, val type :String)



}

/**
 * This class is used to deserialize the response to a search for a Track
 */
@Serializable
data class ListTrack(val data: List<DeezerModelClasses.Track>, val total : Int, val next:String)

@Serializable
/**
 * This class is used to deserialize the response to a search for a playlist
 */

data class ListPlaylist(val data: List<DeezerModelClasses.Playlist>, val total: Int,val next: String)
