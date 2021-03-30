package com.example.sharepoint.datastoreoperetion

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import com.example.sharepoint.utils.Constants
import kotlinx.coroutines.flow.first

class DataStoreLocationRepository (context: Context){

    var dataStore = context.createDataStore( name = Constants.DATA_STORE_NAME_LOCATION_PREF)

    // fun show userId from dataStore
    suspend fun showUserForLocationId( key : String ): String?{
        val dataStoreKey = preferencesKey<String>(key)
        val preference = dataStore.data.first()
        return preference[dataStoreKey]
    }
}