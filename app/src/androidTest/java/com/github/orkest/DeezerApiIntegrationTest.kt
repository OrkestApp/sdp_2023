package com.github.orkest

import com.github.orkest.Model.DeezerApiIntegration
import com.github.orkest.Model.DeezerModelClasses
import com.github.orkest.Model.FireStoreDatabaseAPI
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Assert
import org.junit.Test

class DeezerApiIntegrationTest {



    /**
     *
     */
    //@Test Uncomment test online.
    /**
     * This test need to be performed online
     */
    fun JSONresponseIsParsedCorrectly(){
        val value = DeezerApiIntegration().searchSongInDeezerDatabse("petite fille").get()
        print(value.data[0])

        Assert.assertEquals("Petite fille",value.data[0].title)
        Assert.assertEquals("Booba",value.data[0].artist.name)
    }

    /**
     * need to be run with emulator
     */
    @Test
    fun DeezerTokenCorrectlyAddTokenInDb(){

        val mockUsername = "hello"
        val future = FireStoreDatabaseAPI().storeTokenInDatabase(mockUsername,"26")

        future.thenAccept {
        Assert.assertEquals(Firebase.firestore.collection("deezerToken").document(mockUsername).get(),
            mapOf("token" to "26")
        )
            }
    }

    /**
     * Test the different data model classes associated with Deezer alongside their deserialisation
     *
     */

     @Test
     fun userJsonStringProfileIsCorrectlyParsed(){
         val jsonStringFromDeezer = "{\n" +
                 "  \"id\": \"2297625024\",\n" +
                 "  \"name\": \"Zermelo-101\",\n" +
                 "  \"link\": \"https://www.deezer.com/profile/2297625024\",\n" +
                 "  \"picture\": \"https://api.deezer.com/user/2297625024/image\",\n" +
                 "  \"picture_small\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/56x56-000000-80-0-0.jpg\",\n" +
                 "  \"picture_medium\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/250x250-000000-80-0-0.jpg\",\n" +
                 "  \"picture_big\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/500x500-000000-80-0-0.jpg\",\n" +
                 "  \"picture_xl\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/1000x1000-000000-80-0-0.jpg\",\n" +
                 "  \"country\": \"FR\",\n" +
                 "  \"tracklist\": \"https://api.deezer.com/user/2297625024/flow\",\n" +
                 "  \"type\": \"user\"\n" +
                 "}"


        val user = DeezerApiIntegration().deserialise<DeezerModelClasses.User>(jsonStringFromDeezer)
        Assert.assertEquals(user.id,"2297625024")
        Assert.assertEquals(user.name,"Zermelo-101")


     }

    @Test
    fun playlistJsonStringCanBeCorrectlyDeserialize(){
        val jsonStringFromDeezer = "{\n" +
                "      \"id\": 3645740262,\n" +
                "      \"title\": \"100% Eminem\",\n" +
                "      \"public\": true,\n" +
                "      \"nb_tracks\": 38,\n" +
                "      \"link\": \"https://www.deezer.com/playlist/3645740262\",\n" +
                "      \"picture\": \"https://api.deezer.com/playlist/3645740262/image\",\n" +
                "      \"picture_small\": \"https://e-cdns-images.dzcdn.net/images/playlist/8e917792796412110f79996f4ae53b09/56x56-000000-80-0-0.jpg\",\n" +
                "      \"picture_medium\": \"https://e-cdns-images.dzcdn.net/images/playlist/8e917792796412110f79996f4ae53b09/250x250-000000-80-0-0.jpg\",\n" +
                "      \"picture_big\": \"https://e-cdns-images.dzcdn.net/images/playlist/8e917792796412110f79996f4ae53b09/500x500-000000-80-0-0.jpg\",\n" +
                "      \"picture_xl\": \"https://e-cdns-images.dzcdn.net/images/playlist/8e917792796412110f79996f4ae53b09/1000x1000-000000-80-0-0.jpg\",\n" +
                "      \"checksum\": \"7b538f4d7fa916fdedfabb6a0e059b35\",\n" +
                "      \"tracklist\": \"https://api.deezer.com/playlist/3645740262/tracks\",\n" +
                "      \"creation_date\": \"2017-10-02 10:07:26\",\n" +
                "      \"md5_image\": \"8e917792796412110f79996f4ae53b09\",\n" +
                "      \"picture_type\": \"playlist\",\n" +
                "      \"user\": {\n" +
                "        \"id\": 1990304482,\n" +
                "        \"name\": \"Deezer Artist Editor\",\n" +
                "        \"tracklist\": \"https://api.deezer.com/user/1990304482/flow\",\n" +
                "        \"type\": \"user\"\n" +
                "      },\n" +
                "      \"type\": \"playlist\"\n" +
                "    }"


        val playlist = DeezerApiIntegration().deserialise<DeezerModelClasses.Playlist>(jsonStringFromDeezer)
        Assert.assertEquals(playlist.id, "3645740262")
        Assert.assertEquals(playlist.nb_tracks, "38")

        Assert.assertEquals(playlist.link,"https://www.deezer.com/playlist/3645740262")
        Assert.assertEquals(playlist.picture,"https://api.deezer.com/playlist/3645740262/image")

    }

      @Test
      fun trackJsonStringCanBeCorrectlyDeserialize(){
          val jsonStringFromDeezer = "{\"id\": \"434591652\",\n" +
                  "      \"readable\": true,\n" +
                  "      \"title\": \"Petite fille\",\n" +
                  "      \"title_short\": \"Petite fille\",\n" +
                  "      \"title_version\": \"\",\n" +
                  "      \"link\": \"https://www.deezer.com/track/434591652\",\n" +
                  "      \"duration\": \"215\",\n" +
                  "      \"rank\": \"834387\",\n" +
                  "      \"explicit_lyrics\": true,\n" +
                  "      \"explicit_content_lyrics\": 1,\n" +
                  "      \"explicit_content_cover\": 0,\n" +
                  "      \"preview\": \"https://cdns-preview-3.dzcdn.net/stream/c-35affe970bf6efc970d8bfd56fcb09f6-5.mp3\",\n" +
                  "      \"md5_image\": \"c35627fb50640d971702930cf469f594\",\n" +
                  "      \"artist\": {\n" +
                  "        \"id\": \"390\",\n" +
                  "        \"name\": \"Booba\",\n" +
                  "        \"link\": \"https://www.deezer.com/artist/390\",\n" +
                  "        \"picture\": \"https://api.deezer.com/artist/390/image\",\n" +
                  "        \"picture_small\": \"https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/56x56-000000-80-0-0.jpg\",\n" +
                  "        \"picture_medium\": \"https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/250x250-000000-80-0-0.jpg\",\n" +
                  "        \"picture_big\": \"https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/500x500-000000-80-0-0.jpg\",\n" +
                  "        \"picture_xl\": \"https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/1000x1000-000000-80-0-0.jpg\",\n" +
                  "        \"tracklist\": \"https://api.deezer.com/artist/390/top?limit=50\",\n" +
                  "        \"type\": \"artist\"\n" +
                  "      },\n" +
                  "      \"album\": {\n" +
                  "        \"id\": \"52475732\",\n" +
                  "        \"title\": \"Trône\",\n" +
                  "        \"cover\": \"https://api.deezer.com/album/52475732/image\",\n" +
                  "        \"cover_small\": \"https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/56x56-000000-80-0-0.jpg\",\n" +
                  "        \"cover_medium\": \"https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/250x250-000000-80-0-0.jpg\",\n" +
                  "        \"cover_big\": \"https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/500x500-000000-80-0-0.jpg\",\n" +
                  "        \"cover_xl\": \"https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/1000x1000-000000-80-0-0.jpg\",\n" +
                  "        \"md5_image\": \"c35627fb50640d971702930cf469f594\",\n" +
                  "        \"tracklist\": \"https://api.deezer.com/album/52475732/tracks\",\n" +
                  "        \"type\": \"album\"\n" +
                  "      },\n" +
                  "      \"type\": \"track\"\n" +
                  "    }"

        val track = DeezerApiIntegration().deserialise<DeezerModelClasses.Track>(jsonStringFromDeezer)

        Assert.assertEquals(track.title,"Petite fille")
        Assert.assertEquals(track.id,"434591652")
        Assert.assertEquals(track.album.title,"Trône")
      }


}