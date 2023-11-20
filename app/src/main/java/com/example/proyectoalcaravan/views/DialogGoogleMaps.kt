package com.example.proyectoalcaravan.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.proyectoalcaravan.R
import com.example.proyectoalcaravan.databinding.FragmentGoogleMapsBinding
import com.example.proyectoalcaravan.viewmodels.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DialogGoogleMaps : DialogFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private var _binding: FragmentGoogleMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoogleMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.goBack.setOnClickListener {
            dismiss() // Dismiss the DialogFragment when "Go Back" is clicked
        }

        // Other onViewCreated logic
    }
    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    addMarker(latLng)
                    moveCamera(latLng)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

//    private fun moveCamera(latLng: LatLng) {
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
//
//    }

    private fun moveCamera(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM)
        map.animateCamera(cameraUpdate, 1000, null) // 1000 milliseconds (1 second) animation duration
    }


    private fun saveLocation() {
        val latLng = map.cameraPosition.target
        Log.e("latitude", "${latLng.latitude}")
        Log.e("longitudee", "${latLng.longitude}")

        viewModel.latitude.value = latLng.latitude
        viewModel.longitude.value = latLng.longitude
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        map.setOnMapClickListener {
            Log.e("latitudee", "${it.latitude}")
            Log.e("longitudee", "${it.longitude}")
            viewModel.latitude.postValue(it.latitude)
            viewModel.longitude.postValue(it.longitude)


            addMarker(it)
            moveCamera(it)
        }

        var location = viewModel.latitude.value?.let { viewModel.longitude.value?.let { it1 ->
            LatLng(it,
                it1
            )
        } }
        if (location != null) {
            addMarker(location)
//            moveCamera(location)
        }




        val defaultPosition = LatLng(9.8891078, -67.3939925)
        val position = LatLng(viewModel.latitude.value ?: 9.8891078,  viewModel.longitude.value ?: -67.3939925)
        moveCamera(defaultPosition)


    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun addMarker(latLng: LatLng) {

        map.clear()
        map.addMarker(MarkerOptions().position(latLng))
        saveLocation()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val DEFAULT_ZOOM = 15f
    }

    override fun onMapClick(p0: LatLng) {
//        addMarker()
        Log.e("log", "$p0")
        saveLocation()
    }
}