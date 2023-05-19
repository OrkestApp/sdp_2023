package com.github.orkest.data

import android.net.Uri
import com.github.orkest.R
import java.time.LocalDateTime

data class Post(var username: String = "",
                var date: OrkestDate = OrkestDate(LocalDateTime.now(Constants.DB_ZONE_ID)),
                var profilePicId: Int = R.drawable.blank_profile_pic,
                var postDescription: String = "Post Description",
                var song: Song = Song(),
                var nbLikes: Int = 0,
                var likeList: MutableList<String> = ArrayList(),
                var nbComments : Int = 0,
                var media: String = "",
                var isMediaVideo: Boolean = false)
