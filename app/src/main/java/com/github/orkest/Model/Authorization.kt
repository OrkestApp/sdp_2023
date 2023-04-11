package com.github.orkest.Model

import android.app.Activity
import android.content.Intent
import android.util.Base64
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import java.security.MessageDigest


/**
 * This class is used to get the access token from the Spotify API
 *
 * It implements methods for the authorization code flow with PKCE Flow. It
 * is the recommended authorization flow if you’re implementing authorization
 * in a mobile app, single page web app, or any other type of application where
 * the client secret can’t be safely stored
 */
class Authorization {

    companion object{

        private val CODE_VERIFIER = generateRandomString()

        /**
         * Code verifier
         *
         * @return a random string with a length between 43 and 128 characters
         */
        fun generateRandomString(): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..128)
                .map { allowedChars.random() }
                .joinToString("")
        }

        /**
         * Generate cryptographic digest of a variable
         */
        private fun calculateDigest(data: ByteArray): ByteArray {
            val digest = MessageDigest.getInstance("SHA-256")
            return digest.digest(data)
        }

        /**
         * Code challenge
         *
         * @param codeVerifier the code verifier
         * @return the code challenge
         */
        private fun generateCodeChallenge(codeVerifier: String): String{
            val codeVerifierBytes = codeVerifier.toByteArray()
            val codeChallengeBytes = calculateDigest(codeVerifierBytes)
            return Base64.encodeToString(
                codeChallengeBytes,
                Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE
            )
        }

        fun requestUserAuthorization(activity: Activity): Intent =
            AuthorizationClient.createLoginActivityIntent(
                activity,
                AuthorizationRequest.Builder(
                    Providers.SPOTIFY.CLIENT_ID,
                    AuthorizationResponse.Type.CODE,
                    Providers.SPOTIFY.REDIRECT_URI
                )
                    .setScopes(
                        arrayOf(
                            "user-library-read", "user-library-modify",
                            "app-remote-control", "user-read-currently-playing"
                        )
                    )
                    .setCustomParam("code_challenge_method", "S256")
                    .setCustomParam("code_challenge", generateCodeChallenge(CODE_VERIFIER))
                    .build()
            )

        fun getLoginActivityTokenIntent(code: String, activity: Activity): Intent =
            AuthorizationClient.createLoginActivityIntent(
                activity,
                AuthorizationRequest.Builder(
                    Providers.SPOTIFY.CLIENT_ID,
                    AuthorizationResponse.Type.TOKEN,
                    Providers.SPOTIFY.REDIRECT_URI
                )
                    .setCustomParam("grant_type", "authorization_code")
                    .setCustomParam("code", code)
                    .setCustomParam("code_verifier", CODE_VERIFIER)
                    .build()
            )

        }
    }
