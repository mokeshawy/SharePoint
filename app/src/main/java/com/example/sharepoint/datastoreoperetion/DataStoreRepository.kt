package com.example.yshop.datastoreoperetion

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import com.example.sharepoint.utils.Constants
import com.google.maps.GeoApiContext
import kotlinx.coroutines.flow.first

class DataStoreRepository(context: Context) {

    var dataStore = context.createDataStore(name = Constants.DATA_STORE_USER_NAME_KEY)


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

    // fun show userId from dataStore
    suspend fun showUserId( key : String ): String?{
        val dataStoreKey = preferencesKey<String>(key)
        val preference = dataStore.data.first()
        return preference[dataStoreKey]
    }

    suspend fun showImage(key : String): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }
}