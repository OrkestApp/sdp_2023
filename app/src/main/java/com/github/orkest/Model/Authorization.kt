package com.github.orkest.Model

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import okhttp3.*
import java.io.IOException

import java.util.concurrent.CompletableFuture


class Authorization {

    companion object{

        fun getAccessToken(): String? {
            val client_id = Providers.SPOTIFY.CLIENT_ID // Your client id
            val client_secret = "1fc2f06625b8415ea071cf3cf46d390b" // Your secret

            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build()
            val request = client_id?.let { Credentials.basic(it, client_secret) }?.let {
                Request.Builder()
                    .url("https://accounts.spotify.com/api/token")
                    .addHeader("Authorization", it)
                    .post(requestBody)
                    .build()
            }

            val tokenFuture = CompletableFuture<String>()
            if (request != null) {
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        tokenFuture.completeExceptionally(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            tokenFuture.completeExceptionally(IOException("Unexpected code $response"))
                        }
                        val jsonObject = response.body?.string()?.let { JSONObject(it) }
                        val token = jsonObject?.getString("access_token")
                        tokenFuture.complete(token)
                    }
                })
            }

            val token = tokenFuture.get()

            return token
        }
    }
}