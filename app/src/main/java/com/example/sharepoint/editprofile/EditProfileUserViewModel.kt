package com.example.sharepoint.editprofile

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharepoint.addpostfragment.AddPostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import okhttp3.Dispatcher

class EditProfileUserViewModel : ViewModel() {


    lateinit var dataStore  : DataStore<Preferences>

    companion object{
        var NAME_KEY    = "name"
        var PASS_KEY    = "password"
        var MAIL_KEY    = "email"
        var PHONE_KEY   = "phone"
        var IMAGE_KEY   = "image"
        var UID_KEY     = "userId"
    }

    var editName    = MutableLiveData<String>("")
    var editPhone   = MutableLiveData<String>("")

    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference("UserRef")
    var myStorage           = FirebaseStorage.getInstance().reference





    // fun edit profile with out email and password
    fun editProfileUser( context: Context , imageUri : Uri , editTextName : EditText , editTextPhone : EditText ){

        if(editName.value!!.trim().isEmpty()){
            editTextName.error = "Please enter name"
        }else if(editPhone.value!!.trim().isEmpty()){
            editTextPhone.error = " Please enter phone"
        }else{

            var alert = AlertDialog.Builder(context)
            alert.setTitle("you need change information")
            alert.setMessage("click yes will go change your information")
            alert.setPositiveButton("yes"){dialog,which ->

                var userId = firebaseAuth.currentUser?.uid

                var refStorage : StorageReference = myStorage.child("Photo/"+System.currentTimeMillis())
                refStorage.putFile(imageUri).addOnCompleteListener { itSaveImage ->
                    if(itSaveImage.isSuccessful){
                        refStorage.downloadUrl.addOnSuccessListener { itImageDownload->
                            viewModelScope.launch {
                                var map = HashMap<String, String>()
                                map["name"]     = editName.value!!.toString()
                                map["password"] = showPassword(PASS_KEY).toString()
                                map["email"]    = showEmail(MAIL_KEY).toString()
                                map["phone"]    = editPhone.value!!.toString()
                                map["userId"]   = userId.toString()
                                map["image"]    = itImageDownload.toString()

                                userReference.child(userId.toString()).setValue(map)
                            }
                        }
                    }
                }
            }
            alert.setNegativeButton("no",null)
            alert.create().show()
        }
    }

    suspend fun showPassword( key : String ): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference  = dataStore.data.first()
        return preference[dataStoreKey]
    }

    suspend fun showEmail( key : String ): String?{

        var dataStoreKey = preferencesKey<String>(key)
        var preference  = dataStore.data.first()
        return preference[dataStoreKey]
    }
}