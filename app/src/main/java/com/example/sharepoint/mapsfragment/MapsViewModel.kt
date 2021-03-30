package com.example.sharepoint.mapsfragment
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharepoint.datastoreoperetion.DataStoreLocationRepository
import com.example.sharepoint.retrofitconnection.ServiceBuilder
import com.example.sharepoint.utils.Constants
import com.example.yshop.datastoreoperetion.DataStoreRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MapsViewModel : ViewModel() {

   lateinit var dataStore: DataStore<Preferences>

    lateinit var fusedLocationProviderClient  : FusedLocationProviderClient

    var googleDirection = MutableLiveData<MapsModel>()


    fun callGoogleDirection( context : Context , map : GoogleMap , textViewRoutes : TextView , textViewDuration : TextView ){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        var firebaseDatabase        = FirebaseDatabase.getInstance()
        var userLocationRef         = firebaseDatabase.getReference(Constants.REF_LOCATION)

        if(ActivityCompat.checkSelfPermission( context ,  android.Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            //ActivityCompat.requestPermissions(context as Activity,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }else{

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->

                var origin = "${location?.latitude} , ${location?.longitude}"

                viewModelScope.launch {

                    userLocationRef.child( DataStoreLocationRepository(context).showUserForLocationId(Constants.UID_KEY).toString()).addValueEventListener( object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            var latitude    = snapshot.child(Constants.LATITUDE_KEY).value.toString()
                            var longitude   = snapshot.child(Constants.LONGITUDE_KEY).value.toString()

                            var destination = "${latitude} , ${ longitude}"

                            CoroutineScope(Dispatchers.IO).async {

                                var response = ServiceBuilder.makeRetrofitMaps().direction(origin , destination , "AIzaSyB4ski2q_cAYGl2LA4aHyjU3LALRspXZvM")

                                CoroutineScope(Dispatchers.Main).async {
                                    googleDirection.value = response.body()

                                    drawDirections(location?.latitude!!.toDouble() , location?.longitude!!.toDouble() , latitude.toDouble() , longitude.toDouble() , map , context)
                                    textViewRoutes.text   = response.body()!!.routes[0].legs[0].distance.text
                                    textViewDuration.text = response.body()!!.routes[0].legs[0].duration.text
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
        }
    }


    suspend fun drawDirections(startLat : Double, startLon:Double, endLat : Double, endLon : Double, map: GoogleMap , context: Context) {
        dataStore = context.createDataStore( name = Constants.DATA_STORE_USER_NAME_KEY)

        val path: MutableList<LatLng> = ArrayList()
        val context = GeoApiContext().setQueryRateLimit(3).setApiKey("AIzaSyB4ski2q_cAYGl2LA4aHyjU3LALRspXZvM")
                .setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS).setWriteTimeout(1, TimeUnit.SECONDS)
        var latLngOrigin = LatLng(startLat,startLon)
        var latLngDestination = LatLng(endLat,endLon)
        viewModelScope.launch {
            map.addMarker(MarkerOptions().position(latLngOrigin).title("My Location : ${showName(Constants.NAME_KEY)}").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
        }
        //map.addMarker(MarkerOptions().position(latLngDestination).title("Location"))

        // animate camera to show map with 2 points only
        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        builder.include(latLngOrigin)
        builder.include(latLngDestination)
        val bounds: LatLngBounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0)
        map.animateCamera(cu)

        Log.i("Draw","$startLat,$startLon")
        val req = DirectionsApi.getDirections(context, "$startLat,$startLon", "$endLat,$endLon")
        try {
            val res = req.await()

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.size > 0) {
                val route = res.routes[0]
                if (route.legs != null) {
                    for (i in route.legs.indices) {
                        val leg = route.legs[i]
                        if (leg.steps != null) {
                            for (j in leg.steps.indices) {
                                val step = leg.steps[j]
                                if (step.steps != null && step.steps.size > 0) {
                                    for (k in step.steps.indices) {
                                        val step1 = step.steps[k]
                                        val points1 = step1.polyline
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            val coords1 = points1.decodePath()
                                            for (coord1 in coords1) {
                                                path.add(LatLng(coord1.lat, coord1.lng))
                                            }
                                        }
                                    }
                                } else {
                                    val points = step.polyline
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        val coords = points.decodePath()
                                        for (coord in coords) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
        }
        if (path.size > 0) {
            val opts = PolylineOptions().addAll(path).color(Color.BLUE).width(8f)
            map.addPolyline(opts)
        }
    }

    suspend fun showName(key : String): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }
}

