package com.github.orkest.Model

import com.github.orkest.Constants
import com.github.orkest.R
import java.time.LocalDateTime

data class Post(var username: String = "",
                var date: OrkestDate = OrkestDate(LocalDateTime.now(Constants.DB_ZONE_ID)),
                var profilePicId: Int = R.drawable.blank_profile_pic,
                var postDescription: String = "Post Description",
                var song: Song = Song(),
                var likes: Int = 0,
                var comments: MutableList<Comment> = ArrayList(),
                var nbComments : Int = 0)
