package com.github.emilehreich.bootcamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class ApiActivity : AppCompatActivity() {

    @Inject lateinit var boredApi : BoredApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)

        val button = findViewById<Button>(R.id.boredButton)
        button.setOnClickListener {
            try {
                // Print the activity received into the text view
               makeActivityRequest()
            } catch (e : java.lang.Exception) {
                findViewById<TextView>(R.id.Activity).text = "Something went wrong"
            }
        }
    }

    /**
     * Returns the body of an activity proposal, can be null
     */
    private fun makeActivityRequest() {

        boredApi.getActivity().enqueue(object : Callback<BoredActivity> {
            override fun onResponse(call: Call<BoredActivity>, response: Response<BoredActivity>) {
                if (response.isSuccessful) {
                     var activity = response.body()

                    if (activity != null) {
                        findViewById<TextView>(R.id.Activity).text = "Activity: " + activity!!.activity
                        findViewById<TextView>(R.id.Type).text = "Type: " + activity!!.type
                        findViewById<TextView>(R.id.Participants).text = "Participants: " + activity!!.participants.toString()
                        findViewById<TextView>(R.id.Price).text = "Price: " + activity!!.price
                    }else{
                        throw java.lang.NullPointerException()
                    }
                } else {
                    throw HttpException(response)
                }
            }

            override fun onFailure(call: Call<BoredActivity>, t: Throwable) {
                throw t
            }
        })
    }

}


