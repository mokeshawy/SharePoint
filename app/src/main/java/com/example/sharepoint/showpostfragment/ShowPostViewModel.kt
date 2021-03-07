package com.example.sharepoint.showpostfragment

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShowPostViewModel : ViewModel() {

    lateinit var dataStore : DataStore<Preferences>
    companion object{
        var NAME_KEY    = "name"
        var IMAGE_KEY   = "image"
    }

    fun userProfile(context : Context, imageView: ImageView , textName : TextView){
        dataStore = context.createDataStore(name = "UserPref")

        viewModelScope.launch {
            textName.text = showName(NAME_KEY)
            Picasso.get().load(showImage(IMAGE_KEY)).into(imageView)
        }
    }

    var showPostForAllUser = MutableLiveData<ArrayList<ShowPostModel>>()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var postReference       = firebaseDatabase.getReference("PostRef")
    var array               = ArrayList<ShowPostModel>()

    fun showPost(){
        array = ArrayList()
        postReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for ( ds in snapshot.children){

                    var name    = ds.child("name").value.toString()
                    var post    = ds.child("post").value.toString()
                    var image   = ds.child("postImage").value.toString()

                    array.add(ShowPostModel(name , post , image))
                }
                showPostForAllUser.value = array
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    suspend fun showImage(key : String): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }

    suspend fun showName( key : String ): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference = dataStore.data.first()
        return preference[dataStoreKey]

    }
}