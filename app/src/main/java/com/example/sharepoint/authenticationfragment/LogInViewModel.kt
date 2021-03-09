package com.example.sharepoint.authenticationfragment

import android.app.AlertDialog
import android.content.Context
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.sharepoint.R
import com.example.sharepoint.datastorerepository.DataStoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class LogInViewModel() : ViewModel(){

    lateinit var  dataStore : DataStore<Preferences>
    // companion object for key dataStore
    companion object{
        var NAME_KEY    = "name"
        var PASS_KEY    = "password"
        var MAIL_KEY    = "email"
        var PHONE_KEY   = "phone"
        var IMAGE_KEY   = "image"
        var UID_KEY     = "userId"
    }

    var editEmail = MutableLiveData<String>("")
    var editPass  = MutableLiveData<String>("")

    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference("UserRef")

    // fun log in
    fun logIn( context : Context , view : View , email : EditText , pass : EditText , progressBar: ProgressBar){

        if(editEmail.value!!.trim().isEmpty()){
            email.error = " Please enter your email"
        }else if(editPass.value!!.trim().isEmpty()){
            pass.error = "Please enter your password"
        }else if(editPass.value!!.length < 6){
            pass.error = "the password is not less than 6 number"
        }else{

            var alert = AlertDialog.Builder(context)
            alert.setTitle("Are you need login")
            alert.setMessage("after click 'yes' account will be go log in")
            alert.setPositiveButton("yes"){dialog,which->
                progressBar.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(editEmail.value!! , editPass.value!! ).addOnCompleteListener {
                    if(it.isSuccessful){
                        if(firebaseAuth.currentUser?.isEmailVerified!!){

                            // save id for user login in variable
                            var userId = firebaseAuth.currentUser?.uid
                            var userEmail = firebaseAuth.currentUser?.email

                            Toast.makeText(context,"Welcome ${editEmail.value}",Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_homeFragment)

                            progressBar.visibility = View.INVISIBLE

                            // show data for user login by currentUser?.uid and save dataStore
                            userReference.child(userId.toString()).addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    var name    = snapshot.child("name").value.toString()
                                    var phone   = snapshot.child("phone").value.toString()
                                    var userId  = snapshot.child("userId").value.toString()
                                    var image   = snapshot.child("image").value.toString()

                                    // Save data by dataStore
                                    dataStore = context.createDataStore( name = "UserPref")
                                    viewModelScope.launch {
                                        saveValue( name, editPass.value!! , editEmail.value!! , phone, userId , image )
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })

                        }else{

                            Toast.makeText(context,"Please check your email confirmation",Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.INVISIBLE
                        }
                    }else{
                        Toast.makeText(context,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }
            alert.setNegativeButton("no",null)
            alert.create().show()

        }
    }
    // fun log in

    // fun save value by DataStore
    suspend fun saveValue(name : String , pass : String , mail : String , phone : String , userId : String,  image : String){

        dataStore.edit { userPref ->
            userPref[preferencesKey<String>(NAME_KEY)]  = name
            userPref[preferencesKey<String>(PASS_KEY)]  = pass
            userPref[preferencesKey<String>(MAIL_KEY)]  = mail
            userPref[preferencesKey<String>(PHONE_KEY)] = phone
            userPref[preferencesKey<String>(UID_KEY)]   = userId
            userPref[preferencesKey<String>(IMAGE_KEY)] = image
        }
    }

}