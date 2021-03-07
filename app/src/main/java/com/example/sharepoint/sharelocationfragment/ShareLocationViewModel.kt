package com.example.sharepoint.sharelocationfragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharepoint.homefragment.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShareLocationViewModel : ViewModel() {
    // create dataStore
    lateinit var dataStore: DataStore<Preferences>
    companion object{
        var NAME_KEY    = "name"
        var PHONE_KEY   = "phone"
        var UID_KEY     = "userId"
        var IMAGE_KEY   = "image"
    }

    lateinit var fusedLocationClient  : FusedLocationProviderClient
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var firebaseAuth        = FirebaseAuth.getInstance()
    var userLocationRef     = firebaseDatabase.getReference("MyLocation")
    var userId              = firebaseAuth.currentUser?.uid

    // fun send location for user log in
    fun senYourLocation( context: Context ){
        dataStore = context.createDataStore(name = "UserPref")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if(ActivityCompat.checkSelfPermission(context ,Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){


        }else{
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->

               var latitude     = "${location?.latitude}"
               var longitude    = "${location?.longitude}"

                var map = HashMap<String , String>()

                map["latitude"]     = latitude
                map["longitude"]    = longitude

                viewModelScope.launch {

                    userLocationRef.child(showUserId(UID_KEY).toString()).setValue(map)

                }


                Toast.makeText(context,"Your location send",Toast.LENGTH_SHORT).show()
             }
        }
    }


    // Show data form dataStore
    fun userProfile(context: Context, nameText : TextView, imageProfile : ImageView){
        dataStore = context.createDataStore(name = "UserPref")
        viewModelScope.launch {
            nameText.text = showdName(HomeViewModel.NAME_KEY)
            Picasso.get().load(showImage(HomeViewModel.IMAGE_KEY)).into(imageProfile)

        }
    }

    // fun show name from dataStore
    suspend fun showdName( key : String ): String?{
        val dataStoreKey = preferencesKey<String>(key)
        val preference = dataStore.data.first()
        return preference[dataStoreKey]
    }


    // fun show phone from dataStore
    suspend fun showUserId( key : String ): String?{
        val dataStoreKey = preferencesKey<String>(key)
        val preference = dataStore.data.first()
        return preference[dataStoreKey]
    }

    // fun show image from dataStore
    suspend fun showImage( key : String ): String?{
        val dataStoreKey = preferencesKey<String>(key)
        val preference = dataStore.data.first()
        return preference[dataStoreKey]

    }
}