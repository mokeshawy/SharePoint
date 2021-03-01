package com.example.sharepoint.datastorerepository

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(context: Context) {

    private object PreferenceKey{

        val name    = preferencesKey<String>("name")
        val pass    = preferencesKey<String>("password")
        val mail    = preferencesKey<String>("email")
        val phone   = preferencesKey<String>("phone")
        val image   = preferencesKey<String>("image")

    }

    val dataStore : DataStore<Preferences> = context.createDataStore( name = "UserPref")

    suspend fun saveData( name : String , pass : String , mail : String , phone : String , image : String){
        dataStore.edit { preference ->

            preference[PreferenceKey.name] = name
            preference[PreferenceKey.pass] = pass
            preference[PreferenceKey.mail] = mail
            preference[PreferenceKey.phone] = phone
            preference[PreferenceKey.image] = image

        }
    }

    // read name
    val readMyName : Flow<String> = dataStore.data.map {preference->

       val myName = preference[PreferenceKey.name] ?: ""
        myName
    }

    // read pass
    val readMyPass : Flow<String> = dataStore.data.map {preference->

         val myPass = preference[PreferenceKey.pass] ?: ""
        myPass
    }

    // read mail
    val readMyMail : Flow<String> = dataStore.data.map {preference->

        val myMail = preference[PreferenceKey.mail] ?: ""
        myMail

    }


    // read phone
    val readMyPhone : Flow<String> = dataStore.data.map {preference->

        val myPhone =  preference[PreferenceKey.phone] ?: ""
        myPhone
    }

    // read image
    val readMyImage : Flow<String> = dataStore.data.map { preference->

       val myImage = preference[PreferenceKey.image] ?: ""
        myImage
    }
}


