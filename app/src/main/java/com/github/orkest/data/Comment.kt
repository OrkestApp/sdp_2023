package com.github.orkest.data

import java.time.LocalDateTime

/**
 * Class defining comments users are able to publish under other people's posts
 * @param username: username of the user publishing the comment
 * @param text: the actual content of the comment
 * @param postId: id of the post under which the comment is published
 */
data class Comment(
    val username: String = Constants.CURRENT_LOGGED_USER,
    var date: OrkestDate = OrkestDate(LocalDateTime.now(Constants.DB_ZONE_ID)),
    val text: String = ""
    )