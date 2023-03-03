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

        // Set up view binding
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap

            //Setting zoom controls and limits
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.setMinZoomPreference(8.0f)
            mMap.setMaxZoomPreference(17.0f)

            val EPFL = LatLng(46.520536, 6.568318)

            mMap.moveCamera(CameraUpdateFactory.newLatLng(EPFL))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EPFL, 15f)) //Set zoom level

            //Add a click listener for our marker
            mMap.setOnMarkerClickListener { marker: Marker ->
                //Displaying a toast message on clicking on marker
                Toast.makeText(
                    applicationContext,
                    "Coordinates of " + marker.title + " : " + marker.position.latitude + ", " + marker.position.longitude,
                    Toast.LENGTH_SHORT
                ).show()
                //keeps the marker's title on top of the icon
                false
            }

            // Observe the list of markers and add them to the map
            viewModel.markers.observe(this) { markers ->
                markers.forEach { marker ->
                    mMap.addMarker(marker)
                }
            }
        }

        // Set up the ViewModel
        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]

        // Fetch the list of markers
        viewModel.fetchMarkers()
    }

}