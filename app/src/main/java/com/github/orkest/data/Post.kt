package com.github.orkest.data

import com.github.orkest.R
import java.time.LocalDateTime

data class Post(var username: String = "",
                var date: OrkestDate = OrkestDate(LocalDateTime.now(Constants.DB_ZONE_ID)),
                var profilePicId: Int = R.drawable.blank_profile_pic,
                var postDescription: String = "Post Description",
                var song: Song = Song(),
                var likes: Int = 0,
                var nbComments : Int = 0)
