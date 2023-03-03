package com.github.emilehreich.bootcamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


open class MapsViewModel: ViewModel() {
    // Define a LiveData object to hold the list of markers
    private val _markers = MutableLiveData<List<MarkerOptions>>()
    open val markers: LiveData<List<MarkerOptions>> = _markers


    // Define a function to fetch the list of markers
    fun fetchMarkers() {
        // Make a network request or query a local database to get the list of markers
        val markers = listOf(
            createMarker("EPFL", LatLng(46.520536, 6.568318), BitmapDescriptorFactory.HUE_CYAN ),
            createMarker("Satellite", LatLng(46.520544, 6.567825), BitmapDescriptorFactory.HUE_ROSE )
        )

        // Update the LiveData object with the list of markers
        _markers.postValue(markers)
    }

    private fun createMarker(name: String, coordinates: LatLng, color: Float): MarkerOptions {
        return MarkerOptions()
            .title(name)
            .position(coordinates)
            .draggable(true)
            .icon(BitmapDescriptorFactory.defaultMarker(color))
    }
}



