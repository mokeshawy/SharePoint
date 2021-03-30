package com.example.sharepoint.sharelocationfragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.sharepoint.R
import com.example.sharepoint.utils.Constants
import com.example.yshop.datastoreoperetion.DataStoreRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ShareLocationViewModel : ViewModel() {

    lateinit var fusedLocationClient  : FusedLocationProviderClient
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var firebaseAuth        = FirebaseAuth.getInstance()
    var userLocationRef     = firebaseDatabase.getReference(Constants.REF_LOCATION)
    var userId              = firebaseAuth.currentUser?.uid

    // fun send location for user log in
    fun senYourLocation( context: Context ){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if(ActivityCompat.checkSelfPermission(context ,Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){


        }else{
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->

               var latitude     = "${location?.latitude}"
               var longitude    = "${location?.longitude}"

                var map = HashMap<String , String>()

                map[Constants.LATITUDE_KEY]     = latitude
                map[Constants.LONGITUDE_KEY]    = longitude

                viewModelScope.launch {

                    userLocationRef.child(DataStoreRepository(context).showUserId(Constants.UID_KEY).toString()).setValue(map)

                }

                Toast.makeText(context,"Your location send",Toast.LENGTH_SHORT).show()
             }
        }
    }

   // fun logout
    fun userLogout( view : View){
        firebaseAuth.signOut()
        Navigation.findNavController(view).navigate(R.id.action_shareLocationFragment_to_logInFragment)
    }

    // Show data form dataStore
    fun userProfile(context: Context, nameText : TextView, imageProfile : ImageView){
        viewModelScope.launch {
            nameText.text = DataStoreRepository(context).showName(Constants.NAME_KEY)
            Picasso.get().load(DataStoreRepository(context).showImage(Constants.IMAGE_KEY)).into(imageProfile)

        }
    }
}