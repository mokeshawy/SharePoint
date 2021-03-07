package com.example.sharepoint.addpostfragment

import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.preferencesSetKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.sharepoint.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.joda.time.DateTimeUtils.currentTimeMillis

class AddPostViewModel : ViewModel() {

    lateinit var dataStore : DataStore<Preferences>
    companion object{
        var NAME_KEY    = "name"
        var IMAGE_KEY   = "image"
    }

    var editAddSharePost = MutableLiveData<String>("")

    fun showUserForUser(context : Context, imageView: ImageView , textViewName : TextView){
        dataStore = context.createDataStore(name = "UserPref")
        viewModelScope.launch {
            textViewName.text = showName(NAME_KEY)
            Picasso.get().load(showImage(IMAGE_KEY)).into(imageView)
        }
    }

    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var myStorage           = FirebaseStorage.getInstance().reference
    var addPostReference    = firebaseDatabase.getReference("PostRef")

    fun addPost(context : Context, view : View , imageUri : Uri , editAddPost : EditText , progressBar: ProgressBar){

        var userId = firebaseAuth.currentUser?.uid

        if(editAddSharePost.value!!.isEmpty()){
            editAddPost.error = "Please enter your post"
        }else{
            progressBar.visibility = View.VISIBLE

            var postStorage : StorageReference = myStorage.child("PostPhoto/"+System.currentTimeMillis())
            postStorage.putFile(imageUri).addOnCompleteListener { itSaveImage ->
                if(itSaveImage.isSuccessful){
                    postStorage.downloadUrl.addOnSuccessListener { itDownloadImage ->

                        var map = HashMap<String , String>()
                            map["post"]         = editAddSharePost.value!!.toString()
                            map["postImage"]    = itDownloadImage.toString()
                        viewModelScope.launch {
                            map["name"]         = showName(NAME_KEY).toString()
                        }
                        addPostReference.child(userId.toString()).setValue(map)
                        Navigation.findNavController(view).navigate(R.id.action_addPostFragment_to_showPostFragment)
                        progressBar.visibility = View.INVISIBLE
                    }
                }else{
                    Toast.makeText(context,"Please try agin", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
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