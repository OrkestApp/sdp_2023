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

    private fun uploadFile(path: String, data: ByteArray): UploadTask {
        val ref = storageRef.child(path)
        return ref.putBytes(data)
    }

    fun uploadProfilePic(data: ByteArray): UploadTask {
        val path = currentUserPath + "profile_pic.jpg"
        return uploadFile(path, data)
    }

    private fun uploadFile(path: String, uri: Uri): UploadTask {
        val ref = storageRef.child(path)
        return ref.putFile(uri)
    }

    fun uploadProfilePic(uri: Uri): UploadTask {
        val path = currentUserPath + "profile_pic.jpg"
        return uploadFile(path, uri)
    }

    fun uploadPostPic(uri: Uri): UploadTask {
        val path = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/post1.jpg"
        return uploadFile(path, uri)
    }

    fun uploadVideo(uri: Uri): UploadTask {
        val path = "User-${Constants.CURRENT_LOGGED_USER[0].uppercase()}/${Constants.CURRENT_LOGGED_USER}/post1.jpg"
        return uploadFile("", uri)
    }


    // ============== FETCHING FROM STORAGE ==============

    fun fetchPic(path: String): Task<ByteArray> {
        val ONE_MEGABYTE: Long = 1 * 1024 * 1024

        val ref = storageRef.child(path)
        return ref.getBytes(ONE_MEGABYTE)
    }

    fun fetchProfilePic(username: String): Task<ByteArray> {
        val path = "User-${username[0].uppercase()}/${username}/profile_pic.jpg"
        return fetchPic(path)
    }

    // TODO
    fun fetchVideo(): Task<ByteArray>? {
        return null
    }


}