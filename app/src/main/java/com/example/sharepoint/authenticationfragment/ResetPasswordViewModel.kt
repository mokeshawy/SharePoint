package com.example.sharepoint.authenticationfragment

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.example.sharepoint.R

class ResetPasswordViewModel : ViewModel() {

    var editEmail = MutableLiveData<String>("")
    var firebaseAuth = FirebaseAuth.getInstance()


    fun resetPassword(context: Context, view: View, editTextEmail: EditText , progressBar: ProgressBar) {

        if (editEmail.value!!.trim().isEmpty()) {
            editTextEmail.error = "Please enter your email"
        } else {

            var alert = AlertDialog.Builder(context)
            alert.setTitle("you need reset password")
            alert.setMessage("after enter your email will reset password")
            alert.setPositiveButton("yes"){dialog,which->

                progressBar.visibility = View.VISIBLE
                firebaseAuth.sendPasswordResetEmail(editEmail.value!!.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Send Email reset to ${editEmail.value!!}", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(view).navigate(R.id.action_resetPasswordFragment_to_logInFragment)
                        progressBar.visibility = View.INVISIBLE
                    } else{
                        Toast.makeText( context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }
            alert.setNegativeButton("no",null)
            alert.create().show()
        }
    }

}