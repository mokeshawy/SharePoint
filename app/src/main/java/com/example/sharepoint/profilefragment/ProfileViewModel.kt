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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    lateinit var dataStore: DataStore<Preferences>
    companion object{
        var NAME_KEY    = "name"
        var PASS_KEY    = "password"
        var MAIL_KEY    = "email"
        var PHONE_KEY   = "phone"
        var IMAGE_KEY   = "image"
        var UID_KEY     = "userId"
    }



    fun userProfile(context: Context,
                    view : View,
                    imageViewProfile : ImageView,
                    textViewName : TextView ,
                    textViewEmail : TextView ,
                    textViewPhone : TextView ){

        dataStore = context.createDataStore(name = "UserPref")

        viewModelScope.launch {

            Picasso.get().load(showImage(IMAGE_KEY)).into(imageViewProfile)
            textViewName.text   = showName(NAME_KEY)
            textViewEmail.text  = showEmail(MAIL_KEY)
            textViewPhone.text  = showPhone(PHONE_KEY)
        }

    }

    suspend fun showImage(key : String): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }

    suspend fun showName(key : String): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }

    suspend fun showEmail(key : String): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }

    suspend fun showPhone(key : String): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }
}