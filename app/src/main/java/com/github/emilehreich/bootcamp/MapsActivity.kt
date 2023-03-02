package com.github.emilehreich.bootcamp

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.emilehreich.bootcamp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Setting zoom controls and limits
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setMinZoomPreference(8.0f)
        mMap.setMaxZoomPreference(17.0f)

        val EPFL = LatLng(46.520536, 6.568318)

        //Extract EPFL's address
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(EPFL.latitude, EPFL.longitude, 1)
        val address: String = addresses!![0].getAddressLine(0)

        // Add a marker at EPFL and move the camera
        mMap.addMarker(MarkerOptions()
            .position(EPFL)
            .title("EPFL")
            .draggable(true)
            .snippet(address)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(EPFL))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EPFL, 15f)) //Set zoom level

        //Add a marker at Satellite
        val Satellite = LatLng(46.520544, 6.567825)
        mMap.addMarker(MarkerOptions()
            .position(Satellite)
            .title("Satellite")
            .draggable(true)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))

        //Add a click listener for our marker
        mMap.setOnMarkerClickListener { marker: Marker ->
            //Displaying a toast message on clicking on marker
            Toast.makeText(applicationContext, "Coordinates of " + marker.title + " : " + marker.position.latitude + ", " + marker.position.longitude, Toast.LENGTH_SHORT).show()
            //keeps the marker's title on top of the icon
            false
        }
    }



}
