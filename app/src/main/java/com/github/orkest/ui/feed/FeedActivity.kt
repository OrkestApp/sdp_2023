package com.github.orkest.View.feed

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.github.orkest.data.Constants
import com.github.orkest.data.Post
import com.github.orkest.data.Song
import com.github.orkest.R
import com.github.orkest.domain.FireStoreDatabaseAPI
import com.github.orkest.domain.FirebaseStorageAPI
import com.github.orkest.domain.persistence.AppDatabase
import com.github.orkest.domain.persistence.AppEntities
import com.github.orkest.ui.feed.PostViewModel
import com.github.orkest.ui.feed.CommentActivity
import com.github.orkest.ui.feed.isVideo
import com.github.orkest.ui.feed.mediaURI
import com.github.orkest.ui.sharing.SharingComposeActivity
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture


/**
 * Composable function for the feed screen
 * Represents the view of the MVVM pattern
 */
@Composable
fun FeedActivity(database: AppDatabase, context: Context, viewModel: PostViewModel) {
    //Add a list of posts
    val listPosts = remember {
        mutableStateOf(ArrayList<Post>().toList())
    } //Add the .toList() to always store an immutable collection to avoid unpredictable behavior

    // Remember the coroutine scope and the lazy list state
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    // Remember the swipe refresh state
    val isRefreshing = remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing.value)

    if(FireStoreDatabaseAPI.isOnline(context)) {
        //if there is internet connexion, get posts from the firestore database and update the cache
        refreshData(viewModel, listPosts)

        CompletableFuture.runAsync{
            val postEntities = listPosts.value.map { post ->
                AppEntities.Companion.PostEntity(
                    id = (0..100000).random(),
                    username = post.username,
                    date = post.date,
                    profilePicId = post.profilePicId,
                    postDescription = post.postDescription,
                    song = post.song,
                    nbLikes = post.nbLikes,
                    likeList = post.likeList,
                    nbComments = post.nbComments,
                    media = post.media,
                    isMediaVideo = post.isMediaVideo
                )
            }

            database.postsDao().insertPosts(postEntities)
        }
    } else {
        //if no internet connection, get posts from the cache
        CompletableFuture.runAsync {
            val cachedPosts: List<AppEntities.Companion.PostEntity> =
                database.postsDao().getAllPosts()
            listPosts.value = cachedPosts.map { postEntity ->
                Post(
                    username = postEntity.username,
                    date = postEntity.date,
                    profilePicId = postEntity.profilePicId,
                    postDescription = postEntity.postDescription,
                    song = postEntity.song,
                    nbLikes = postEntity.nbLikes,
                    likeList = postEntity.likeList,
                    nbComments = postEntity.nbComments,
                    media = postEntity.media,
                    isMediaVideo = postEntity.isMediaVideo
                )
            }
        }
    }

    // Wrap the LazyColumn with SwipeRefresh
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            if(FireStoreDatabaseAPI.isOnline(context)) {
            coroutineScope.launch {
                    // Update isRefreshing to true
                    isRefreshing.value = true

                    refreshData(viewModel, listPosts)

                    // Update isRefreshing to false
                    isRefreshing.value = false
                }
            }
        }
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            items(listPosts.value) { post ->
                DisplayPost(post = post, viewModel = viewModel)

            }
        }
    }


}

/**
 * Function to refresh the data displayed in the feed
 * @param viewModel the view model to get the data from
 * @param listPosts the list of posts to update
 */
private fun refreshData(viewModel: PostViewModel, listPosts: MutableState<List<Post>>) {
    viewModel.getRecentPosts(Constants.DUMMY_LAST_CONNECTED_TIME)
        .whenComplete { t, u ->
            if (t != null) {
                listPosts.value = t
            }
        }
}

/**
 * Composable function to display a post, can be reused for the profile page
 * @param post the post to display
 */
@Composable
fun DisplayPost(viewModel: PostViewModel, post: Post) {

    Row(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color.DarkGray)
            .fillMaxWidth()
    ) {

        Column {
            // Display the user profile pic
            /**TODO change when fetching from database is possible. For now, it avoids error of vectorized image**/
            ProfilePic(R.drawable.blank_profile_pic)
            //Display the reaction buttons
            Reaction(viewModel, post, context = LocalContext.current)
        }

        Column(modifier = Modifier.padding(10.dp, top = 10.dp, end = 10.dp)) {
            //Add the user's username
            Username(post.username)
            // Display the post's content
            PostDescription(post.postDescription)
            Spacer(modifier = Modifier.height(10.dp))
            // Display the post's song
            SongCard(post.song)
            Spacer(modifier = Modifier.height(10.dp))

            if(post.media.isNotEmpty()) {
                var uri by remember { mutableStateOf( Uri.parse(post.media))}
                if(isVideo) {
                    FirebaseStorageAPI.fetchPostVideo(post).thenApply { uri = it }
                } else {
                    FirebaseStorageAPI.fetchPostPic(post).thenApply { uri = it }
                }
                CapturedMedia(capturedUri = uri!!, isVideo = post.isMediaVideo)
            }

        }
    }
}

@Composable
fun CapturedMedia(
    capturedUri: Uri,
    isVideo: Boolean
) {
    val context = LocalContext.current
    lateinit var exoPlayer: ExoPlayer

    Box(modifier = Modifier.fillMaxSize()) {
        if (isVideo){
            //Displays the taken video
            exoPlayer = remember(context) {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(capturedUri))
                    prepare()
                }
            }
            AndroidView(
                factory = { context ->
                    StyledPlayerView(context).apply {
                        player = exoPlayer
                    }
                },
                modifier = Modifier
                    .size(400.dp)
                    .testTag("Captured Video")
            )

        } else {
            //Displays the captured Image
            Image(
                painter = rememberImagePainter(data = capturedUri),
                contentDescription = "Captured Image",
                modifier = Modifier.size(400.dp)
            )
        }
    }
}

@Composable
private fun Username(username: String) {
    //Add the user's username
    Text(text = username, fontSize = 14.sp, fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .clickable { })
}

@Composable
private fun ProfilePic(profilePicId: Int) {
    //Add the user's profile pic
    Image(
        painter = painterResource(id = profilePicId),
        contentDescription = "Profile Picture of the user",
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
            .height(40.dp)
            .width(40.dp)
            .clip(shape = CircleShape)
            .clickable { }
    )
}

@Composable
private fun PostDescription(postDescription: String) {
    //Add the post's content
    Text(
        text = postDescription,
        fontSize = 15.sp,
        color = Color.White,
        maxLines = 2
    )
}


/**
 * Composable function that displays the song's info and a play button
 * Can be reused for the sharedWithMe page and profile page
 * @param song the song to display
 */
@Composable
fun SongCard(song: Song) {

    //Row containing the song's album pic, info and play button
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            //.wrapContentSize()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color.hsl(54f, 1f, 0.5f))
            .padding(end = 10.dp)
    ) {

        //Add the song's info at the left of the card
        SongInfo(song)

        Spacer(modifier = Modifier.width(5.dp))

        //Add a play button at the right of the card
        PlayButton(song)
    }
}

@Composable
private fun SongInfo(song: Song) {

    Row(Modifier.padding(10.dp)) {
        //Add the song's picture at the left of the card
        Image(
            painter = painterResource(id = R.drawable.album_cover),
            contentDescription = "Cover of the album of the song Rude Boy by Rihanna",
            modifier = Modifier
                .height(80.dp)
                .clip(shape = RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(verticalArrangement = Arrangement.SpaceEvenly) {

            //Add the song's title at the right of the album
            Text(text = song.Title, fontSize = 25.sp)
            //Add the song's artist at the right of the album
            Text(text = song.Artist, color = Color.Gray, fontSize = 18.sp)
            //Add the song's album at the right of the album
            Text(text = song.Album, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun PlayButton(song: Song) {
    val isPlayed = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Icon(painter = if (!isPlayed.value) painterResource(id = R.drawable.play_button)
    else painterResource(id = R.drawable.pause_button),
        contentDescription = if (!isPlayed.value) "Play button" else "Pause button",
        modifier = Modifier
            .height(50.dp)
            .width(50.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable {
                Constants.playMusicButtonClicked(song, isPlayed, context)
            })
}

@Composable
private fun LikeButton(viewModel: PostViewModel, post: Post, context: Context) {

    //Declaring variables to update the UI
    val isPostLiked = remember{ mutableStateOf(false) }
    val nbLikes = remember { mutableStateOf(post.nbLikes) }

    //Fetching initial values from database
    viewModel.isPostLiked(post).thenApply { isPostLiked.value = it }
    viewModel.getNbLikes(post).thenApply { nbLikes.value = it }

    val buttonColor = if (isPostLiked.value) Color.Yellow else Color.White

    Column {
        //Displaying the like button
        Icon(
            painter = painterResource(id = R.drawable.black_like_icon),
            contentDescription = "Like button",
            tint = buttonColor,
            modifier = Modifier
                .testTag("like_button")
                .height(20.dp)
                .width(20.dp)
                .clickable {
                    if(FireStoreDatabaseAPI.isOnline(context)){
                        viewModel
                            .updatePostLikes(post, !isPostLiked.value)
                            .thenApply {
                                //Updating the isPostLiked value accordingly
                                isPostLiked.value = !isPostLiked.value
                                //Updating the value of getNbLikes only after the updatePostLikes future has been completed
                                viewModel
                                    .getNbLikes(post)
                                    .thenApply { nbLikes.value = it }
                            }
                    }
                    else{
                        Toast.makeText(context, "No internet connection. Unable to like.", Toast.LENGTH_LONG).show()
                    }
                },
        )
        //Displaying the number of likes for this post
        Text(
            text = "${nbLikes.value}",
            color = buttonColor,
            modifier = Modifier
                .padding(5.dp)
                .testTag("Number of likes")
        )
    }

}

@Composable
private fun Reaction(viewModel: PostViewModel, post: Post, context: Context) {
    //val context = LocalContext.current
    Column(modifier = Modifier.padding(20.dp)) {

        // Create the like button
        LikeButton(viewModel, post = post, context)
        Spacer(modifier = Modifier.height(10.dp))

        //Create the comment button
        val context = LocalContext.current
        IconButton(
            modifier = Modifier
                .testTag("comment_button")
                .height(20.dp)
                .width(20.dp),
            onClick = {
                context.startActivity(
                    Intent(context, CommentActivity::class.java)
                        .putExtra("post_date", post.date.toString())
                        .putExtra("post_username", post.username)
                )
            }
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.comment_icon),
                contentDescription = "comment_button",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        //Create the share button
        ReactionIcon(R.drawable.share_icon,"Share button", "share_button"){
            //Launch an intent to the sharingActivity
            context.startActivity(Intent(context, SharingComposeActivity::class.java)
                .putExtra(Constants.SONG_NAME, post.song.Title)
                .putExtra(Constants.SONG_ARTIST, post.song.Artist))
        }
    }
}

/* TODO modularize in next sprint */

@Composable
private fun ReactionIcon(iconId: Int, contentDescription:String, testTag: String, onClick: () -> Unit = {}){
    Icon(painter = painterResource(id = iconId),
        contentDescription = contentDescription,
        tint = Color.White,
        modifier = Modifier
            .testTag(testTag)
            .height(20.dp)
            .width(20.dp)
            .clickable { onClick() })
}
