package com.example.sharepoint.editprofile

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.*
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
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
import com.example.sharepoint.R
import com.example.sharepoint.utils.Constants

class EditProfileUserViewModel : ViewModel() {


    lateinit var dataStore  : DataStore<Preferences>

    var editName    = MutableLiveData<String>("")
    var editPhone   = MutableLiveData<String>("")

    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Constants.REF_USER)
    var myStorage           = FirebaseStorage.getInstance().reference





    // fun edit profile with out email and password
    fun editProfileUser(context: Context, view : View, imageUri : Uri, editTextName : EditText, editTextPhone : EditText, progressBar: ProgressBar){

        if(editName.value!!.trim().isEmpty()){
            editTextName.error = "Please enter name"
        }else if(editPhone.value!!.trim().isEmpty()){
            editTextPhone.error = " Please enter phone"
        }else{

            var alert = AlertDialog.Builder(context)
            alert.setTitle("you need change information")
            alert.setMessage("click yes will go change your information")
            alert.setPositiveButton("yes"){dialog,which ->
                progressBar.visibility = View.VISIBLE
                var userId = firebaseAuth.currentUser?.uid

                var refStorage : StorageReference = myStorage.child("Photo/"+System.currentTimeMillis())
                refStorage.putFile(imageUri).addOnCompleteListener { itSaveImage ->
                    if(itSaveImage.isSuccessful){
                        refStorage.downloadUrl.addOnSuccessListener { itImageDownload->

                                var map = HashMap<String, Any>()
                                map["name"]     = editName.value!!.toString()
                                map["phone"]    = editPhone.value!!.toString()
                                map["image"]    = itImageDownload.toString()
                                userReference.child(userId.toString()).updateChildren(map)

                            Toast.makeText(context,"information has bin edit", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.action_editProfileUserFragment_to_profileFragment)
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
                }
            }
            alert.setNegativeButton("no",null)
            alert.create().show()
        }
    }
}