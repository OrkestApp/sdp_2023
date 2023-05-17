package com.github.orkest.domain

import com.github.orkest.data.Constants
import java.net.HttpURLConnection
import java.net.URL

interface HttpHandler {

    /**
     * Opens a http connection to the @url
     * @param url the url of the disered destination of the connection
     * @param PostOrGET "POST" if you want to do a POST request, GET otherwise
     *
     * return the HttpUrlConnection
     */
     private fun openRequest(url : URL, PostOrGET  : String) : HttpURLConnection{
        val openConnection = url.openConnection() as HttpURLConnection
        openConnection.requestMethod = PostOrGET

        return openConnection

    }

    /**
     * Open a Post Request
     */

    fun openPostRequest(url : URL) : HttpURLConnection{
        return openRequest(url,"POST")
    }

    /**
     * Open a Get request
     */
    fun openGetRequest(url : URL) : HttpURLConnection{
        return openRequest(url,"GET")
    }

    /**
     * This method starts a Post or Get Http connection
     * @param url the url to wich we will make the request
     * @param post true if we want to make a post request, else it is a get request
     *
     * @return the HTTP response or the Constants.HTTP_FAIL_CODE
     */
    fun PostOrGEtAndGetResponse(url : URL, post : Boolean) : String{

        var connection : HttpURLConnection
        if (post){
            connection = openPostRequest(url)
        }
        else{
            connection = openGetRequest(url)
        }

        return if(connection.responseCode == HttpURLConnection.HTTP_OK){
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            Constants.HTTP_FAIL_CODE
        }


    }

}