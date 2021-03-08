package com.example.sharepoint.settingfragment

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharepoint.addpostfragment.AddPostViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    lateinit var dataStore  : DataStore<Preferences>

    companion object{
        var NAME_KEY    = "name"
        var PASS_KEY    = "password"
        var MAIL_KEY    = "email"
        var PHONE_KEY   = "phone"
        var IMAGE_KEY   = "image"
        var UID_KEY     = "userId"
    }

    fun userProfile(context : Context, imageView: ImageView, textViewName : TextView){
        dataStore = context.createDataStore(name = "UserPref")
        viewModelScope.launch {
            textViewName.text = showName(AddPostViewModel.NAME_KEY)
            Picasso.get().load(showImage(AddPostViewModel.IMAGE_KEY)).into(imageView)
        }
    }

    suspend fun showName( key : String ): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference  = dataStore.data.first()
        return preference[dataStoreKey]
    }

    suspend fun showImage( key : String ): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference  = dataStore.data.first()
        return preference[dataStoreKey]
    }
}