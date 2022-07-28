package com.example.cryptodetails.ui.map

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.cryptodetails.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.GeoApiContext
import java.util.*

class MapsFragment : SupportMapFragment() {

    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        try {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            (requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager).requestLocationUpdates(
                LocationManager.FUSED_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE
            ) {
                currentMarker?.remove() // removing the previous marker, so that we can set a new marker.
                addPolyLines(googleMap, it)
                setupMarker(googleMap, it)
            }
        } catch (ex: Exception) {
            Toast.makeText(
                context, "An exception occurred when fetching current location", Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
        }
    }

    private fun addPolyLines(googleMap: GoogleMap, currentLocation: Location) {
        googleMap.addPolyline(
            PolylineOptions()
                .add(LatLng(currentLocation.latitude, currentLocation.longitude))
                .add(LatLng(currentLocation.latitude + 0.15, currentLocation.longitude - 0.1))
                .add(LatLng(currentLocation.latitude + 0.1, currentLocation.longitude + 0.05))
                .add(LatLng(currentLocation.latitude, currentLocation.longitude))
                .color(Color.RED)
                .width(10f)
        )
    }

    private var currentMarker: Marker? = null
    private lateinit var geoAPIContext: GeoApiContext

    private fun setupMarker(googleMap: GoogleMap, location: Location) {
        val currentLocation = LatLng(location.latitude, location.longitude)
        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        loadProfileImage(acct?.photoUrl!!) { icon ->
            // options are: MAP_TYPE_NORMAL, MAP_TYPE_SATELLITE, MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

            currentMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title("${getString(R.string.current_location)} of ${acct.displayName}")
                    .icon(icon)
            )
            googleMap.setOnMarkerClickListener {
                showAddressDialog(currentLocation)
                return@setOnMarkerClickListener true
            }
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(currentLocation, MAPS_CITY_VIEW)
            )
        }
    }

    private fun showAddressDialog(currentLocation: LatLng) {
        AlertDialog.Builder(requireContext())
            .setTitle("Address")
            .setMessage("address is ${getAddress(currentLocation)}")
            .setCancelable(true)
            .setPositiveButton("Ok") { dialog, _ -> dialog?.dismiss() }
            .show()
    }

    private fun getAddress(currentLocation: LatLng): String {
        val addresses: List<Address> = Geocoder(requireContext(), Locale.getDefault())
            .getFromLocation(
                currentLocation.latitude,
                currentLocation.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        return addresses[0].getAddressLine(0)
    }

    private fun loadProfileImage(imageUri: Uri, callback: (resource: BitmapDescriptor) -> Unit) {
        Glide.with(this).asBitmap()
            .load(imageUri)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    callback(BitmapDescriptorFactory.fromBitmap(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        requestLocationPermission()
        geoAPIContext =
            GeoApiContext.Builder().apiKey(getString(R.string.google_maps_api_key)).build()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(onMapReadyCallback)
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 103
        private const val MAPS_PLANET_VIEW = 1f
        private const val MAPS_LANDMASS_VIEW = 5f
        private const val MAPS_CITY_VIEW = 10f
        private const val MAPS_STREET_VIEW = 15f
        private const val MAPS_BUILDING_VIEW = 20f

        const val LOCATION_REFRESH_TIME = 15000L // 15 seconds to update
        const val LOCATION_REFRESH_DISTANCE = 500f // 500 meters to update
    }

    private fun checkLocationPermission() = ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission() {
        if (checkLocationPermission())
            return

        activity?.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
        )

        Toast.makeText(context, "Location Permission requested", Toast.LENGTH_LONG).show()
    }
}