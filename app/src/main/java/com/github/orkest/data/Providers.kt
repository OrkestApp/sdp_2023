package com.github.orkest.data

enum class Providers(val value:String, val CLIENT_ID: String?, val REDIRECT_URI: String?){
    /**
     * Enum representing the different music providers
     *
     * value (String) : Name of the provider
     * CLIENT_ID (String) : ID linked to the provider API
     * REDIRECT_URI (String) : Redirect URI of the provider
     */
    SPOTIFY("Spotify",
        "e7ac920406d54975bc79962dec94f4ab",
        "https://com.github.orkest/callback/"),
    DEEZER("Deezer",
        null,
        null),
    APPLE_MUSIC("Apple Music",
        null,
        null)
}