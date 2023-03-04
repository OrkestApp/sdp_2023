package com.github.emilehreich.bootcamp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.emilehreich.bootcamp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater) // Set up view binding
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.uiSettings.isZoomControlsEnabled = true //Setting zoom controls and limits
            mMap.setMinZoomPreference(8.0f)
            mMap.setMaxZoomPreference(17.0f)

            val EPFL = LatLng(46.520536, 6.568318)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EPFL, 15f)) //Set zoom level
            mMap.setOnMarkerClickListener { marker: Marker -> //Add a click listener for our marker
                Toast.makeText(applicationContext, "Coordinates of " + marker.title + " : " + marker.position.latitude + ", " + marker.position.longitude,
                    Toast.LENGTH_SHORT
                ).show()
                false//keeps the marker's title on top of the icon
            }

            viewModel.markers.observe(this) { markers -> // Observe the list of markers and add them to the map
                markers.forEach { marker -> mMap.addMarker(marker) } }
        }

        viewModel = ViewModelProvider(this)[MapsViewModel::class.java] // Set up the ViewModel
        viewModel.fetchMarkers() // Fetch the list of markers
    }
}