package com.github.orkest.domain

import android.util.Log

interface DeezerMockAPi : DeezerApi {
    override fun fetchTheUserId(access_token: String): String {
        return "{\n" +
                "  \"id\": \"2297625024\",\n" +
                "  \"name\": \"Zermelo-101\",\n" +
                "  \"lastname\": \"Rocher\",\n" +
                "  \"firstname\": \"Jean-françois\",\n" +
                "  \"email\": \"jf-rocher@outlook.fr\",\n" +
                "  \"status\": 2,\n" +
                "  \"birthday\": \"0000-00-00\",\n" +
                "  \"inscription_date\": \"2018-09-04\",\n" +
                "  \"gender\": \"\",\n" +
                "  \"link\": \"https://www.deezer.com/profile/2297625024\",\n" +
                "  \"picture\": \"https://api.deezer.com/user/2297625024/image\",\n" +
                "  \"picture_small\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/56x56-000000-80-0-0.jpg\",\n" +
                "  \"picture_medium\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/250x250-000000-80-0-0.jpg\",\n" +
                "  \"picture_big\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/500x500-000000-80-0-0.jpg\",\n" +
                "  \"picture_xl\": \"https://e-cdns-images.dzcdn.net/images/user/5136e5875050a362da76693a723290a0/1000x1000-000000-80-0-0.jpg\",\n" +
                "  \"country\": \"CH\",\n" +
                "  \"lang\": \"en\",\n" +
                "  \"is_kid\": false,\n" +
                "  \"explicit_content_level\": \"explicit_display\",\n" +
                "  \"explicit_content_levels_available\": [\n" +
                "    \"explicit_display\",\n" +
                "    \"explicit_no_recommendation\",\n" +
                "    \"explicit_hide\"\n" +
                "  ],\n" +
                "  \"tracklist\": \"https://api.deezer.com/user/2297625024/flow\",\n" +
                "  \"type\": \"user\"\n" +
                "}"
    }

    override fun addANewSongToOrkestPlayList(
        access_token: String,
        playlistId: String,
        trackId: String
    ): String {
        return "done"
    }

    override fun searchAsong(songName: String, artistName: String): String {
        Log.d("ENTER MOCK", songName)
        return "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": \"434591652\",\n" +
                "      \"readable\": true,\n" +
                "      \"title\": \"Petite fille\",\n" +
                "      \"title_short\": \"Petite fille\",\n" +
                "      \"title_version\": \"\",\n" +
                "      \"link\": \"https://www.deezer.com/track/434591652\",\n" +
                "      \"duration\": \"215\",\n" +
                "      \"rank\": \"865026\",\n" +
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
                "    },\n" +
                "    {\n" +
                "      \"id\": \"1081609622\",\n" +
                "      \"readable\": true,\n" +
                "      \"title\": \"Petite fille\",\n" +
                "      \"title_short\": \"Petite fille\",\n" +
                "      \"title_version\": \"\",\n" +
                "      \"link\": \"https://www.deezer.com/track/1081609622\",\n" +
                "      \"duration\": \"215\",\n" +
                "      \"rank\": \"283271\",\n" +
                "      \"explicit_lyrics\": true,\n" +
                "      \"explicit_content_lyrics\": 1,\n" +
                "      \"explicit_content_cover\": 0,\n" +
                "      \"preview\": \"https://cdns-preview-7.dzcdn.net/stream/c-775b6a57bca057968b815f63809c88c0-5.mp3\",\n" +
                "      \"md5_image\": \"073da400c4fd8d35340b22bc886859ed\",\n" +
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
                "        \"id\": \"173577742\",\n" +
                "        \"title\": \"Moment Détente\",\n" +
                "        \"cover\": \"https://api.deezer.com/album/173577742/image\",\n" +
                "        \"cover_small\": \"https://e-cdns-images.dzcdn.net/images/cover/073da400c4fd8d35340b22bc886859ed/56x56-000000-80-0-0.jpg\",\n" +
                "        \"cover_medium\": \"https://e-cdns-images.dzcdn.net/images/cover/073da400c4fd8d35340b22bc886859ed/250x250-000000-80-0-0.jpg\",\n" +
                "        \"cover_big\": \"https://e-cdns-images.dzcdn.net/images/cover/073da400c4fd8d35340b22bc886859ed/500x500-000000-80-0-0.jpg\",\n" +
                "        \"cover_xl\": \"https://e-cdns-images.dzcdn.net/images/cover/073da400c4fd8d35340b22bc886859ed/1000x1000-000000-80-0-0.jpg\",\n" +
                "        \"md5_image\": \"073da400c4fd8d35340b22bc886859ed\",\n" +
                "        \"tracklist\": \"https://api.deezer.com/album/173577742/tracks\",\n" +
                "        \"type\": \"album\"\n" +
                "      },\n" +
                "      \"type\": \"track\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"918030732\",\n" +
                "      \"readable\": true,\n" +
                "      \"title\": \"Petite fille\",\n" +
                "      \"title_short\": \"Petite fille\",\n" +
                "      \"title_version\": \"\",\n" +
                "      \"link\": \"https://www.deezer.com/track/918030732\",\n" +
                "      \"duration\": \"215\",\n" +
                "      \"rank\": \"118028\",\n" +
                "      \"explicit_lyrics\": true,\n" +
                "      \"explicit_content_lyrics\": 1,\n" +
                "      \"explicit_content_cover\": 0,\n" +
                "      \"preview\": \"https://cdns-preview-7.dzcdn.net/stream/c-775b6a57bca057968b815f63809c88c0-5.mp3\",\n" +
                "      \"md5_image\": \"17263e9a274d646562f0b65af4550006\",\n" +
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
                "        \"id\": \"139231602\",\n" +
                "        \"title\": \"Rap au calme\",\n" +
                "        \"cover\": \"https://api.deezer.com/album/139231602/image\",\n" +
                "        \"cover_small\": \"https://e-cdns-images.dzcdn.net/images/cover/17263e9a274d646562f0b65af4550006/56x56-000000-80-0-0.jpg\",\n" +
                "        \"cover_medium\": \"https://e-cdns-images.dzcdn.net/images/cover/17263e9a274d646562f0b65af4550006/250x250-000000-80-0-0.jpg\",\n" +
                "        \"cover_big\": \"https://e-cdns-images.dzcdn.net/images/cover/17263e9a274d646562f0b65af4550006/500x500-000000-80-0-0.jpg\",\n" +
                "        \"cover_xl\": \"https://e-cdns-images.dzcdn.net/images/cover/17263e9a274d646562f0b65af4550006/1000x1000-000000-80-0-0.jpg\",\n" +
                "        \"md5_image\": \"17263e9a274d646562f0b65af4550006\",\n" +
                "        \"tracklist\": \"https://api.deezer.com/album/139231602/tracks\",\n" +
                "        \"type\": \"album\"\n" +
                "      },\n" +
                "      \"type\": \"track\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"924540802\",\n" +
                "      \"readable\": true,\n" +
                "      \"title\": \"Petite fille\",\n" +
                "      \"title_short\": \"Petite fille\",\n" +
                "      \"title_version\": \"\",\n" +
                "      \"link\": \"https://www.deezer.com/track/924540802\",\n" +
                "      \"duration\": \"215\",\n" +
                "      \"rank\": \"39058\",\n" +
                "      \"explicit_lyrics\": true,\n" +
                "      \"explicit_content_lyrics\": 1,\n" +
                "      \"explicit_content_cover\": 0,\n" +
                "      \"preview\": \"https://cdns-preview-7.dzcdn.net/stream/c-775b6a57bca057968b815f63809c88c0-5.mp3\",\n" +
                "      \"md5_image\": \"15b66268afe93628d9eae32b559a7b0c\",\n" +
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
                "        \"id\": \"140562472\",\n" +
                "        \"title\": \"Se réveiller en musique\",\n" +
                "        \"cover\": \"https://api.deezer.com/album/140562472/image\",\n" +
                "        \"cover_small\": \"https://e-cdns-images.dzcdn.net/images/cover/15b66268afe93628d9eae32b559a7b0c/56x56-000000-80-0-0.jpg\",\n" +
                "        \"cover_medium\": \"https://e-cdns-images.dzcdn.net/images/cover/15b66268afe93628d9eae32b559a7b0c/250x250-000000-80-0-0.jpg\",\n" +
                "        \"cover_big\": \"https://e-cdns-images.dzcdn.net/images/cover/15b66268afe93628d9eae32b559a7b0c/500x500-000000-80-0-0.jpg\",\n" +
                "        \"cover_xl\": \"https://e-cdns-images.dzcdn.net/images/cover/15b66268afe93628d9eae32b559a7b0c/1000x1000-000000-80-0-0.jpg\",\n" +
                "        \"md5_image\": \"15b66268afe93628d9eae32b559a7b0c\",\n" +
                "        \"tracklist\": \"https://api.deezer.com/album/140562472/tracks\",\n" +
                "        \"type\": \"album\"\n" +
                "      },\n" +
                "      \"type\": \"track\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"total\": 4\n" +
                "}"

    }


    override fun createANewPlaylistOnTheUserProfile(
        userId: String,
        access_token: String,
        playlistTitle: String
    ): String {
        return "3645740262"
    }

    class DeezerMockApiImplemented(): DeezerMockAPi{

    }
}