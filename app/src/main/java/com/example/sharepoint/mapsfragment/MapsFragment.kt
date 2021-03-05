package com.example.sharepoint.mapsfragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentMapsBinding
import com.example.sharepoint.retrofitconnection.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MapsFragment : Fragment() {

    lateinit var binding                        : FragmentMapsBinding
    val mapsViewModel                           : MapsViewModel by viewModels()
    lateinit var firebaseDatabase               : FirebaseDatabase
    lateinit var userLocation                   : DatabaseReference
    lateinit var dataStore                      : DataStore<Preferences>

    companion object{
        var UID_KEY     = "userId"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        binding.lifecycleOwner  = this
        binding.mapsModel       = mapsViewModel

        // call operation for firebase
        firebaseDatabase            = FirebaseDatabase.getInstance()
        userLocation                = firebaseDatabase.getReference("MyLocation")
        // operation on for data Store
        dataStore                   = requireActivity().createDataStore(name = "UserPref")

    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        lifecycleScope.launch {
            var ui = showUserId(UID_KEY)
            userLocation.child(ui.toString()).addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    var latitude  = snapshot.child("latitude").value.toString()
                    var longitude = snapshot.child("longitude").value.toString()

                    val location = LatLng(latitude.toDouble(), longitude.toDouble())
                    googleMap.addMarker(MarkerOptions().position(location).title(ui))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,16.0f))

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            // make require permission
            if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }else{
                googleMap.isMyLocationEnabled           = true
            }
            googleMap.uiSettings.isZoomControlsEnabled  = true

            binding.button2.setOnClickListener {
                mapsViewModel.callGoogleDirection(requireActivity(),googleMap , binding.textView2 , binding.textView3)
            }
        }
    }

    suspend fun showUserId(key : String): String?{
        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]
    }
}