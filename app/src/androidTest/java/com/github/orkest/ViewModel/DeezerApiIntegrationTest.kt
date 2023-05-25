package com.github.orkest.ViewModel

import android.content.Intent
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.github.orkest.SearchViewModelTest
import com.github.orkest.domain.*
import com.github.orkest.ui.DeezerWelcomeActivity
import com.github.orkest.ui.search.SearchUserView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class DeezerApiIntegrationTest {


    /**
     * Need to artificialy create the data classes so they can be considered as tested
     */

    /**
     *
     */
    //@Test Uncomment test online.
    /**
     * This test need to be performed online
     */
    fun JSONresponseIsParsedCorrectly(){
        val value = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()).searchSongInDeezerDatabse("petite fille").get()
        print(value.data[0])

        Assert.assertEquals("Petite fille",value.data[0].title)
        Assert.assertEquals("Booba",value.data[0].artist.name)
    }

    /**
     * tested
     */

    fun JsonResponseforUSerId(){
        val value = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()).addANewSongToOrkestPlayList("fre2MZQMJbgEjomDl7WusDOj3p0RIo3g0dhW4G1kVMG5Oa9CGV","11300267884","533497102").get()
        Log.d("HELLO", value.toString())

    }

    /**
     * tested
     */
    fun createPlaylist(){
        val value = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()).createANewPlaylistOnTheUserProfile("2297625024","frsizHK2HlrzljGuf9s09gV0sF1Wbf7fWCnQe4w1wFbDI0zCkqV").get()
        Log.d("HELLO", value.toString())

    }
    /**
     * tested
     */
    fun testGetUserIdIndatabase(){
        val value = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()).fetchTheUserIdInTheDeezerDatabase("fre2MZQMJbgEjomDl7WusDOj3p0RIo3g0dhW4G1kVMG5Oa9CGV").get()
        Log.d("TEST",value.id)
        Log.d("TEST",value.name)
    }

    /**
     * need to be run with emulator
     */
    @Test
    fun DeezerTokenCorrectlyAddTokenInDb(){

        val mockUsername = "hello"
        val future = FireStoreDatabaseAPI().storeDeezerInformationsInDatabase(mockUsername,"26")

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


        val user = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()).deserialise<DeezerModelClasses.User>(jsonStringFromDeezer)
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


        val playlist = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()).deserialise<DeezerModelClasses.Playlist>(jsonStringFromDeezer)
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

        val track = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()).deserialise<DeezerModelClasses.Track>(jsonStringFromDeezer)

        Assert.assertEquals(track.title,"Petite fille")
        Assert.assertEquals(track.id,"434591652")
        Assert.assertEquals(track.album.title,"Trône")
      }
    @Test
    fun mockSearchInDatabase(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())

        val x = mockDeezerIntegration.searchSongInDeezerDatabse("pettite fille").get()
        assertEquals("Booba",x.data[0].artist.name)
        assertEquals("Petite fille",x.data[0].title)
        assertEquals("434591652",x.data[0].id)
        assertEquals("215",x.data[0].duration)
        assertEquals(0,x.data[0].explicit_content_cover)
        assertEquals(1,x.data[0].explicit_content_lyrics)
        assertEquals(true,x.data[0].explicit_lyrics)
        assertEquals("https://www.deezer.com/track/434591652",x.data[0].link)
        assertEquals("https://cdns-preview-3.dzcdn.net/stream/c-35affe970bf6efc970d8bfd56fcb09f6-5.mp3",x.data[0].preview)
        assertEquals("c35627fb50640d971702930cf469f594",x.data[0].md5_image)
        assertEquals("865026",x.data[0].rank)
        assertEquals(true,x.data[0].readable)
        assertEquals("Petite fille",x.data[0].title_short)
        assertEquals("",x.data[0].title_version)
        assertEquals("track",x.data[0].type)

        //Need to create the dataclasses artificially  to improve code coverage
        val createAlbum = DeezerModelClasses.Album("","","","","","","","","","")
        assertEquals("390",x.data[0].artist.id)
        assertEquals("Booba",x.data[0].artist.name)
        assertEquals("https://www.deezer.com/artist/390",x.data[0].artist.link)
        assertEquals("https://api.deezer.com/artist/390/image",x.data[0].artist.picture)
        assertEquals("https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/56x56-000000-80-0-0.jpg",x.data[0].artist.picture_small)
        assertEquals("https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/250x250-000000-80-0-0.jpg",x.data[0].artist.picture_medium)
        assertEquals("https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/500x500-000000-80-0-0.jpg",x.data[0].artist.picture_big)
        assertEquals("https://e-cdns-images.dzcdn.net/images/artist/38b687e97c6874e744d305ef2ca8d0d0/1000x1000-000000-80-0-0.jpg",x.data[0].artist.picture_xl)
        assertEquals("https://api.deezer.com/artist/390/top?limit=50",x.data[0].artist.tracklist)
        assertEquals("artist",x.data[0].artist.type)

        val createArtist = DeezerModelClasses.Artist("","","","","","","",",","","")
        assertEquals("52475732",x.data[0].album.id)
        assertEquals("Trône",x.data[0].album.title)
        assertEquals("https://api.deezer.com/album/52475732/image",x.data[0].album.cover)
        assertEquals("https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/56x56-000000-80-0-0.jpg",x.data[0].album.cover_small)
        assertEquals("https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/250x250-000000-80-0-0.jpg",x.data[0].album.cover_medium)
        assertEquals("https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/500x500-000000-80-0-0.jpg",x.data[0].album.cover_big)
        assertEquals("https://e-cdns-images.dzcdn.net/images/cover/c35627fb50640d971702930cf469f594/1000x1000-000000-80-0-0.jpg",x.data[0].album.cover_xl)
        assertEquals("c35627fb50640d971702930cf469f594",x.data[0].album.md5_image)
        assertEquals("https://api.deezer.com/album/52475732/tracks",x.data[0].album.tracklist)
        assertEquals("album",x.data[0].album.type)

        val createTrack = DeezerModelClasses.Track("",true,"","","","","","",true,
            1,0,"","",createArtist,createAlbum,"")

        val createUser = DeezerModelClasses.User("","","","")

        val createPlaylist = DeezerModelClasses.Playlist("","",true,"","","","","","","","","","","","",createUser,"")


    }
    @Test
    fun mockSearchInDatabaseWithEmptySongName(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())

        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.searchSongInDeezerDatabse(null).join()}

    }

    @Test
    fun mockFetchId(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        val x = mockDeezerIntegration.fetchTheUserIdInTheDeezerDatabase("adadad").get()

        val create = DeezerModelClasses.UserExtended("","","","","","","","","","","","","","","","","","","",
            listOf(""),"","")
        assertEquals("Zermelo-101",x.name)
        assertEquals("2297625024",x.id)
        assertEquals("Rocher",x.lastname)
        assertEquals("Jean-françois",x.firstname)
        assertEquals("jf-rocher@outlook.fr",x.email)
        assertEquals("2",x.status)
        assertEquals("0000-00-00",x.birthday)
        assertEquals("2018-09-04",x.inscription_date)
        assertEquals("",x.gender)
        assertEquals("https://www.deezer.com/profile/2297625024",x.link)
        assertEquals("https://api.deezer.com/user/2297625024/image",x.picture)
        assertEquals("https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/56x56-000000-80-0-0.jpg",x.picture_small)
        assertEquals("https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/250x250-000000-80-0-0.jpg",x.picture_medium)
        assertEquals("https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/500x500-000000-80-0-0.jpg",x.picture_big)
        assertEquals("https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/1000x1000-000000-80-0-0.jpg",x.picture_xl)
        assertEquals("CH",x.country)
        assertEquals("en",x.lang)
        assertEquals("false",x.is_kid)
        assertEquals("explicit_display",x.explicit_content_level)
        assertEquals(listOf("explicit_display","explicit_no_recommendation","explicit_hide"),x.explicit_content_levels_available)
        assertEquals("https://api.deezer.com/user/2297625024/flow",x.tracklist)
        assertEquals("user",x.type)

    }

    @Test
    fun mockFetchIdWithEmptyAccessToken(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())

        assertThrows(java.util.concurrent.CompletionException::class.java) { -> mockDeezerIntegration.fetchTheUserIdInTheDeezerDatabase("").join()}

    }

    @Test
    fun mockAddPlaylistToUserProfile(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        val response = mockDeezerIntegration.createANewPlaylistOnTheUserProfile("dummy","dummy").get()
        assertEquals("3645740262",response)
    }

    @Test
    fun mockAddPlaylistToUserProfileFailWithEmptyUserId(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.createANewPlaylistOnTheUserProfile("","dummy").join()}
    }
    @Test
    fun mockAddPlaylistToUserProfileFailWithEmptyAccessToken(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.createANewPlaylistOnTheUserProfile("dummy","").join()}
    }


    @Test
    fun mockAddANewSongToThePlaylist(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        val success  = mockDeezerIntegration.addANewSongToOrkestPlayList("dummy","dummy", "dummy").get()
        assertEquals(true,success)
    }

    @Test
    fun mockAddANewSOngToThePlaylistFailWithEmptyAccessToken(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.addANewSongToOrkestPlayList("","dummy", "dummy").join()}
    }

    @Test
    fun mockAddANewSOngToThePlaylistFailWithEmptyPlaylistId(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.addANewSongToOrkestPlayList("dummy","", "dummy").join()}
        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.addANewSongToOrkestPlayList("dummy","", "").join()}
    }

    @Test
    fun mockAddANewSOngToThePlaylistFailWithEmptyTrackId(){
        val mockDeezerIntegration = DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented())
        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.addANewSongToOrkestPlayList("dummy","dummy", "").join()}
        assertThrows(java.util.concurrent.CompletionException::class.java){mockDeezerIntegration.addANewSongToOrkestPlayList("dummy","", "").join()}
    }

    /**
     * Test for Deezer Welcome Activity
     *
     */

    @get:Rule
    var composeTestRule =  createComposeRule()


    @Test
    fun DeezerUi(){
        composeTestRule.setContent {
            DeezerWelcomeActivity(true).CreateViewForDeezer()
        }
        val button =composeTestRule.onNodeWithText("Start to use Deezer")
        button.assertExists()
        button.performClick()
        composeTestRule.onNodeWithText("Feed").assertIsDisplayed()

    }



    @Test
    fun deezerWelcomeActivityonCreate(){
        val intent = Intent(ApplicationProvider.getApplicationContext(),DeezerWelcomeActivity::class.java).apply {
            putExtra("code","dummy token")
        }
        val scenario = ActivityScenario.launch<DeezerWelcomeActivity>(intent)
        scenario.close()
    }
    @Test
    fun testDeezerWelcomeActivity(){
        composeTestRule.setContent {
            DeezerWelcomeActivity(true).CreateViewForDeezer()
        }
        val button =composeTestRule.onNodeWithText("Start to use Deezer")
        button.assertExists()
        button.performClick()
        /*
        val db = Firebase.firestore
        db.useEmulator("10.0.2.2", 8080)
        db.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
   */


        DeezerWelcomeActivity.WelcomeOperation("CodeVale",DeezerApiIntegration(DeezerMockAPi.DeezerMockApiImplemented()),"BOB").join()

        val info = FireStoreDatabaseAPI().getUserDeezerInformations("BOB").get()
        assertEquals(info.access_token,"CodeVale")
        assertEquals(info.playlistId,"3645740262")
        assertEquals(info.userId,"2297625024")
        //
    }


}