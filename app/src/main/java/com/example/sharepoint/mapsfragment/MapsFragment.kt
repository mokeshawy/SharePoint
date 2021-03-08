package com.example.sharepoint.mapsfragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {

    lateinit var binding                        : FragmentMapsBinding
    val mapsViewModel                           : MapsViewModel by viewModels()
    lateinit var firebaseDatabase               : FirebaseDatabase
    lateinit var userLocation                   : DatabaseReference
    lateinit var userReference                  : DatabaseReference
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

        // connection for viewModel
        binding.lifecycleOwner  = this
        binding.mapsModel       = mapsViewModel

        // call operation for firebase
        firebaseDatabase            = FirebaseDatabase.getInstance()
        userLocation                = firebaseDatabase.getReference("MyLocation")
        userReference               = firebaseDatabase.getReference("UserRef")

        // operation on for data Store
        dataStore                   = requireActivity().createDataStore(name = "UserLocationPref")

    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        lifecycleScope.launch {

            var uId = showUserId(UID_KEY)
            // show LatLng for user you need show location
            userLocation.child(uId.toString()).addValueEventListener( object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    var latitude  = snapshot.child("latitude").value.toString()
                    var longitude = snapshot.child("longitude").value.toString()

                    try{
                        val location = LatLng(latitude.toDouble(), longitude.toDouble())

                        // show user by id into add marker by name
                        userReference.orderByChild("userId").equalTo(uId).addValueEventListener( object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for ( ds in snapshot.children){

                                    var name = ds.child("name").value.toString()
                                    googleMap.addMarker(MarkerOptions().position(location).title(name))
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,16.0f))
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }catch (e:Exception){

                        Toast.makeText( context,"this account not share location",Toast.LENGTH_LONG ).show()

                    }

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

            // button get location with distance and duration
            binding.buttGetLocationId.setOnClickListener {
                mapsViewModel.callGoogleDirection(requireActivity(),googleMap , binding.distanceTextViewId , binding.durationTextViewId)
            }
        }
    }

    suspend fun showUserId(key : String): String?{
        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]
    }
}