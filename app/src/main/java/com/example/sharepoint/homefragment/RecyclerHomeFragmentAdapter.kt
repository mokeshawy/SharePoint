package com.example.sharepoint.homefragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.recyclerview.widget.RecyclerView
import com.example.sharepoint.R
import com.example.sharepoint.databinding.RecyclerHomefragmentItemBinding
import com.example.sharepoint.mapsfragment.MapsFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecyclerHomeFragmentAdapter (private val dataSet: ArrayList<ShowAllUserModel> , var context : Context) :
        RecyclerView.Adapter<RecyclerHomeFragmentAdapter.ViewHolder>() {

    class ViewHolder(var binding : RecyclerHomefragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var myViewHolder = ViewHolder(RecyclerHomefragmentItemBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))

        return myViewHolder
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.binding.textViewNameUserLogInId.text  = dataSet[position].name
        Picasso.get().load(dataSet[position].image).into(viewHolder.binding.viewImageHomeProfileId)
        
        var firebaseDatabase    = FirebaseDatabase.getInstance()
        var userLocationRef     = firebaseDatabase.getReference("UserRef")
        viewHolder.binding.selectUserId.setOnClickListener {

            userLocationRef.orderByChild("userId").equalTo(dataSet[position].uId).addValueEventListener( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(ds in snapshot.children){

                        var userId = ds.child("userId").value.toString()

                        dataStore = context.createDataStore( name = "UserLocationPref")
                        GlobalScope.launch {
                            saveValue(userId)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            Toast.makeText( context, dataSet[position].name , Toast.LENGTH_SHORT).show()
            val activity=it!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment , MapsFragment()).addToBackStack(null).commit()
            Toast.makeText( context, dataSet[position].name , Toast.LENGTH_SHORT).show()

        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}

lateinit var  dataStore : DataStore<Preferences>

suspend fun saveValue( userId : String ){
    dataStore.edit { userPref ->
        userPref[preferencesKey<String>("userId")] = userId
    }
}


