package com.github.orkest.ViewModel

import com.github.orkest.Model.DeezerApiIntegration
import org.junit.Assert
import org.junit.Test

class DeezerApiIntegrationTest {

    @Test
    fun JSONresponseIsParsedCorrectly(){
        val value = DeezerApiIntegration().searchSongInDeezerDatabse("petite fille").get()
        print(value.data[0])

        Assert.assertEquals("Petite fille",value.data[0].title)
        Assert.assertEquals("Booba",value.data[0].artist.name)
    }
}