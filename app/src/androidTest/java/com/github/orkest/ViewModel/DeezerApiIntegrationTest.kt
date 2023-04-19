package com.github.orkest.ViewModel

import com.github.orkest.Model.DeezerApiIntegration
import org.junit.Assert
import org.junit.Test

class DeezerApiIntegrationTest {

    @Test
    fun JSONresponseIsParsedCorrectly(){
        val value = DeezerApiIntegration().searchSongInDeezerDatabse("pettite fille").get()

        Assert.assertEquals("hello",value.toString())
    }
}