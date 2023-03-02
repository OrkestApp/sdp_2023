package com.github.emilehreich.bootcamp

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Call
import retrofit2.http.GET

interface BoredApi {
    @GET("activity")
    fun getActivity(): Call<BoredActivity>
}