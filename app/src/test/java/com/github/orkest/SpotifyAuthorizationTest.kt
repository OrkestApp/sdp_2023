package com.github.orkest

import com.github.orkest.Model.Authorization
import org.junit.Test

class SpotifyAuthorizationTest {

    @Test
    fun testCodeVerifier(){
        val codeVerifier = Authorization.generateRandomString()

        assert(codeVerifier.length <= 128)

    }


}