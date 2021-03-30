package com.example.sharepoint.profilefragment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.preferencesSetKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.sharepoint.R
import com.example.sharepoint.utils.Constants
import com.example.yshop.datastoreoperetion.DataStoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    fun userProfile(context: Context,
                    view : View,
                    imageViewProfile : ImageView,
                    textViewName : TextView ,
                    textViewEmail : TextView ,
                    textViewPhone : TextView ){

        viewModelScope.launch {

            Picasso.get().load(DataStoreRepository(context).showImage(Constants.IMAGE_KEY)).into(imageViewProfile)
            textViewName.text   = DataStoreRepository(context).showName(Constants.NAME_KEY)
            textViewEmail.text  = DataStoreRepository(context).showEmail(Constants.MAIL_KEY)
            textViewPhone.text  = DataStoreRepository(context).showPhone(Constants.PHONE_KEY)
        }

    }

    // firebase connection
    var firebaseAuth        = FirebaseAuth.getInstance()
    fun userLogout( view : View){
        firebaseAuth.signOut()
        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_logInFragment)
    }
}