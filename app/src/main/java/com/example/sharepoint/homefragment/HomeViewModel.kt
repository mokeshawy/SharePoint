package com.example.sharepoint.homefragment
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.sharepoint.R
import com.example.sharepoint.utils.Constants


class HomeViewModel() : ViewModel() {

    lateinit var dataStore: DataStore<Preferences>

    // firebase connection
    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Constants.REF_USER)

    var allUserSignUpShow = MutableLiveData<ArrayList<ShowAllUserModel>>()
    var array             = ArrayList<ShowAllUserModel>()

    // fun show all data for user in recycler view
    fun userProfile(){
        array = ArrayList()

        userReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for( ds in snapshot.children){

                    var name    = ds.child(Constants.CHILD_NAME_KEY).value.toString()
                    var userId  = ds.child(Constants.CHILD_USER_ID_KEY).value.toString()
                    var image   = ds.child(Constants.CHILD_IMAGE_KEY).value.toString()
                    array.add(ShowAllUserModel(name , userId , image))
                }
                allUserSignUpShow.value = array
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun userLogout( view : View){
        firebaseAuth.signOut()
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_logInFragment)
    }


    // Show data form dataStore
    fun readData(context: Context , nameText : TextView , imageProfile : ImageView){
        dataStore = context.createDataStore(name = Constants.DATA_STORE_USER_NAME_KEY)
        viewModelScope.launch {
            nameText.text = showdName(Constants.NAME_KEY)
            Picasso.get().load(showImage(Constants.IMAGE_KEY)).into(imageProfile)

        }
    }

    // fun show name from dataStore
    suspend fun showdName( key : String ): String?{
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