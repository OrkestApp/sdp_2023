package com.github.orkest.domain

import android.net.Uri
import android.util.Log
import com.github.orkest.data.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class FirebaseStorageAPI {

    private val storageRef = FirebaseStorage.getInstance().reference
    private val currentUserPath = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/"

    // ============== UPLOADING TO STORAGE ==============

    /**
     * @param path: path in storage where you want to upload file
     * @param data: the data you want to upload
     */
    private fun uploadFile(path: String, data: ByteArray): UploadTask {
        val ref = storageRef.child(path)
        return ref.putBytes(data)
    }


    /**
     * @param data: profile picture you want to upload to storage
     * will upload the new profile picture to storage at the needed path
     */
    fun uploadProfilePic(data: ByteArray): UploadTask {
        val path = currentUserPath + "profile_pic.jpg"
        return uploadFile(path, data)
    }

    /**
     * @param path: path in storage where you want to upload file
     * @param data: uri of the file you want to upload
     */
    private fun uploadFile(path: String, uri: Uri): UploadTask {
        val ref = storageRef.child(path)
        return ref.putFile(uri)
    }


    /**
     * @param uri: uri of the profile picture you want to upload to storage
     * will upload the new profile picture to storage at the needed path
     */
    fun uploadProfilePic(uri: Uri): UploadTask {
        val path = currentUserPath + "profile_pic.jpg"
        return uploadFile(path, uri)
    }


    /**
     * @param uri: uri of the picture you want to upload to storage
     * will upload the picture associated to a given post
     */
    // TODO must still decide how to organize the folder with post pictures in storage
    fun uploadPostPic(uri: Uri): UploadTask {
        val path = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/post1.jpg"
        return uploadFile(path, uri)
    }

    /**
     * @param uri: uri of the video you want to upload to storage
     * will upload the picture associated to a given post
     */
    fun uploadVideo(uri: Uri): UploadTask {
        val path = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/post1.jpg"
        return uploadFile("", uri)
    }


    // ============== FETCHING FROM STORAGE ==============

    /**
     * @param path: path in storage of the file you want to fetch
     * Fetches a picture at a given path in storage
     */
    private fun fetchPic(path: String): Task<ByteArray> {
        val ONE_MEGABYTE: Long = 1 * 1024 * 1024

        val ref = storageRef.child(path)
        return ref.getBytes(ONE_MEGABYTE)
    }


    /**
     * @param username: usr whose profile picture you want to get from storage
     * Fetches a given user's profile picture from storage
     */
    fun fetchProfilePic(username: String): Task<ByteArray> {
        //path in storage where the picture is located
        val path = "User-${username[0].uppercase()}/${username}/profile_pic.jpg"
        return fetchPic(path)
    }

    // TODO
    fun fetchVideo(): Task<ByteArray>? {
        return null
    }


}