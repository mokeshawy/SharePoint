package com.example.sharepoint.editprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentEditProfileUserBinding

class EditProfileUserFragment : Fragment() {

    lateinit var binding            : FragmentEditProfileUserBinding
    val viewModelEditProfileUser    : EditProfileUserViewModel by viewModels()
    lateinit var imageUri           : Uri

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner          = this
        binding.editProfileUserModel    = viewModelEditProfileUser


        // edit profile
        binding.progressBarEditProfileId.visibility = View.INVISIBLE
        binding.buttEditProfileId.setOnClickListener {
            try{
                viewModelEditProfileUser.editProfileUser(requireActivity() , view , imageUri , binding.editNameEditProfileId , binding.editPhoneEditProfileId , binding.progressBarEditProfileId)
            }catch(e:Exception){
                Toast.makeText(requireActivity() , "Please select image",Toast.LENGTH_SHORT).show()
                binding.progressBarEditProfileId.visibility = View.INVISIBLE
            }
        }

        // select image
        binding.viewImageEditProfileId.setOnClickListener {

            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent , 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){

            imageUri = data?.data!!
            binding.viewImageEditProfileId.setImageURI(imageUri)
        }
    }
}