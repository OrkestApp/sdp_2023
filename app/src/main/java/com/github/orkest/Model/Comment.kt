package com.github.orkest.Model

/**
 * Class defining comments users are able to publish under other people's posts
 * @param username: username of the user publishing the comment
 * @param text: the actual content of the comment
 * @param postId: id of the post under which the comment is published
 */
data class Comment(
    val postId: Int,
    //val commentId: Int,
    val username: String,
    val text: String
    )