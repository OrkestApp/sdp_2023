package com.github.emilehreich.bootcamp

import retrofit2.Call
import retrofit2.http.GET

interface BoredApi {
    @GET("activity")
    fun getActivity(): Call<ApiActivity.BoredActivity>
}