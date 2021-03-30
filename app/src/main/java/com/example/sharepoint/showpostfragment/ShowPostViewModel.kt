package com.example.sharepoint.showpostfragment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.sharepoint.R
import com.example.sharepoint.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShowPostViewModel : ViewModel() {

    lateinit var dataStore : DataStore<Preferences>

    fun userProfile(context : Context, imageView: ImageView , textName : TextView){
        dataStore = context.createDataStore(name = Constants.DATA_STORE_USER_NAME_KEY)

        viewModelScope.launch {
            textName.text = showName(Constants.NAME_KEY)
            Picasso.get().load(showImage(Constants.IMAGE_KEY)).into(imageView)
        }
    }

    var showPostForAllUser = MutableLiveData<ArrayList<ShowPostModel>>()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var postReference       = firebaseDatabase.getReference(Constants.POST_REF_KEY)
    var array               = ArrayList<ShowPostModel>()

    fun showPost(){
        array = ArrayList()
        postReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for ( ds in snapshot.children){

                    var name    = ds.child(Constants.POST_CHILD_NAME_KEY).value.toString()
                    var post    = ds.child(Constants.POST_CHILD_POST_KEY).value.toString()
                    var image   = ds.child(Constants.POST_CHILD_POST_IMAGE_KEY).value.toString()

                    array.add(ShowPostModel(name , post , image))
                }
                showPostForAllUser.value = array
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // firebase connection
    var firebaseAuth        = FirebaseAuth.getInstance()
    fun userLogout( view : View){
        firebaseAuth.signOut()
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_logInFragment)
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