package com.example.sharepoint.showpostfragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.recyclerview.widget.RecyclerView
import com.example.sharepoint.databinding.RecyclerShowpostItemBinding
import com.squareup.picasso.Picasso

class RecyclerShowPostAdapter  (private val dataSet: ArrayList<ShowPostModel>) :
    RecyclerView.Adapter<RecyclerShowPostAdapter.ViewHolder>() {

    class ViewHolder(var binding : RecyclerShowpostItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var myViewHolder = ViewHolder(RecyclerShowpostItemBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))

        return myViewHolder
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.binding.showNameId.text              = dataSet[position].name
        viewHolder.binding.textViewShowWritPostId.text  = dataSet[position].description
        Picasso.get().load(dataSet[position].image).into(viewHolder.binding.showPostImageId)


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
