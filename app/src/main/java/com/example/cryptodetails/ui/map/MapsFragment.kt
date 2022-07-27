package com.example.cryptodetails.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : SupportMapFragment() {

    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        try {
            LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation.addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentLocation = LatLng(it.result.latitude, it.result.longitude)
                    val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
                    loadImage(acct?.photoUrl!!) { icon ->
                        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        googleMap.addMarker(
                            MarkerOptions().position(currentLocation)
                                .title(getString(R.string.current_location))
                                .icon(icon)
                        )
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLocation,
                                MAPS_STREET_VIEW
                            )
                        )
                    }
                } else {
                    Toast.makeText(context, "Location could not be requested", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(
                context, "An exception occurred when fetching current location", Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
        }

        //        newLatLngZoom()
        // 1 earth, 5 continent/country, 10 city, 15 street, 20 building
    }

    private fun loadImage(imageUri: Uri, callback: (resource: BitmapDescriptor) -> Unit) {
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