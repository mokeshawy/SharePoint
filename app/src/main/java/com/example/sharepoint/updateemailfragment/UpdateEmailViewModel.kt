package com.example.sharepoint.updateemailfragment

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.sharepoint.R
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class UpdateEmailViewModel : ViewModel() {

    var editUpdateEmail = MutableLiveData<String>("")


    var firebaseAuth    = FirebaseAuth.getInstance()
    var user            = firebaseAuth.currentUser
    fun updateEmail( context: Context , view: View , editTextEmail : EditText , progressBar: ProgressBar){

        if(editUpdateEmail.value!!.trim().isEmpty()){
            editTextEmail.error = "Please enter new email"
        }else{
            progressBar.visibility = View.VISIBLE
            user!!.updateEmail(editUpdateEmail.value!!).addOnCompleteListener {
                if(it.isSuccessful){
                    firebaseAuth.currentUser?.sendEmailVerification()
                    Toast.makeText(context ,"send verification ${editUpdateEmail.value}",Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_updateEmailFragment_to_logInFragment)
                    progressBar.visibility = View.INVISIBLE
                }else{
                    Toast.makeText(context ,it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}