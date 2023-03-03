package com.github.emilehreich.bootcamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


class MockMapsActivity {
    // Define a LiveData object to hold the list of markers
    private val _markers = MutableLiveData<List<MarkerOptions>>()
    val markers: LiveData<List<MarkerOptions>> = _markers

    // Define a function to simulate the behavior of fetchMarkers
    fun simulateFetchMarkers() {
        // Make a network request or query a local database to get the list of markers
        val markers = listOf(
            MarkerOptions()
                .title("EPFL")
                .position(LatLng(46.520536, 6.568318))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)),
            MarkerOptions()
                .title("Satellite")
                .position(LatLng(46.520544, 6.567825))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )

        // Update the LiveData object with the list of markers
        _markers.postValue(markers)
    }
}

@RunWith(JUnit4::class)
class MapsActivityTest {

    val markers = listOf(
        MarkerOptions()
            .title("EPFL")
            .position(LatLng(46.520536, 6.568318))
            .draggable(true)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)),
        MarkerOptions()
            .title("Satellite")
            .position(LatLng(46.520544, 6.567825))
            .draggable(true)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
    )


    @get:Rule
    val activityRule = ActivityScenarioRule(MapsActivity::class.java)

    @Test
    fun checksIfMapIsDisplayed() {
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

}



