package com.github.orkest.domain

import android.net.Uri
import android.util.Log
import com.github.orkest.data.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.concurrent.CompletableFuture

class FirebaseStorageAPI {

    /*companion object {
        private val storage = FirebaseStorage.getInstance()
    }*/
    private val storageRef = FirebaseStorage.getInstance().reference
    private val currentUserPath = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/"

    // ============== UPLOADING TO STORAGE ==============

    /**
     * @param path: path in storage where you want to upload file
     * @param data: the data you want to upload
     */
    private fun uploadFile(path: String, data: ByteArray): CompletableFuture<Boolean> {
        //val storageRef = FirebaseStorage.getInstance().reference
        val completedFuture = CompletableFuture<Boolean>()
        storageRef.child(path).putBytes(data).addOnSuccessListener(){
            completedFuture.complete(true)
        }.addOnFailureListener {
            completedFuture.complete(false)
        }

        return completedFuture
    }

    /**
     * @param path: path in storage where you want to upload file
     * @param data: uri of the file you want to upload
     */
    private fun uploadFile(path: String, uri: Uri): CompletableFuture<Boolean> {
        val completedFuture = CompletableFuture<Boolean>()
        //val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child(path).putFile(uri).addOnSuccessListener(){
            completedFuture.complete(true)
        }.addOnFailureListener {
            completedFuture.complete(false)
        }
        return completedFuture
    }

    /**
     * @param data: profile picture you want to upload to storage
     * will upload the new profile picture to storage at the needed path
     */
    fun uploadProfilePic(data: ByteArray): CompletableFuture<Boolean> {
        val path = currentUserPath + "profile_pic.jpg"
        return uploadFile(path, data)
    }


    /**
     * @param uri: uri of the profile picture you want to upload to storage
     * will upload the new profile picture to storage at the needed path
     */
    fun uploadProfilePic(uri: Uri): CompletableFuture<Boolean> {
        val path = currentUserPath + "profile_pic.jpg"
        return uploadFile(path, uri)
    }


    /**
     * @param uri: uri of the picture you want to upload to storage
     * will upload the picture associated to a given post
     */
    // TODO must still decide how to organize the folder with post pictures in storage
    fun uploadPostPic(uri: Uri): CompletableFuture<Boolean> {
        val path = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/post1.jpg"
        return uploadFile(path, uri)
    }

    /**
     * @param uri: uri of the video you want to upload to storage
     * will upload the picture associated to a given post
     */
    fun uploadVideo(uri: Uri): CompletableFuture<Boolean> {
        val path = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/post1.jpg"
        return uploadFile("", uri)
    }


    // ============== FETCHING FROM STORAGE ==============

    /**
     * @param path: path in storage of the file you want to fetch
     * Fetches a picture at a given path in storage
     */
    private fun fetchPic(path: String): CompletableFuture<ByteArray> {
        val ONE_MEGABYTE: Long = 1 * 1024 * 1024
        val futurePic = CompletableFuture<ByteArray>()
        storageRef.child(path).getBytes(ONE_MEGABYTE).addOnSuccessListener {
            futurePic.complete(it)
        }.addOnFailureListener {
            Log.e("FirebaseStorageAPI", "Error while fetching picture from storage")
        }
        return futurePic
    }


    /**
     * @param username: usr whose profile picture you want to get from storage
     * Fetches a given user's profile picture from storage
     */
    fun fetchProfilePic(username: String): CompletableFuture<ByteArray> {
        //path in storage where the picture is located
        val path = "User-${username[0].uppercase()}/${username}/profile_pic.jpg"
        return fetchPic(path)
    }


}