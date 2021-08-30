package com.example.sharepoint.addpostfragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentAddPostBinding

class AddPostFragment : Fragment() {

    lateinit var binding    : FragmentAddPostBinding
    private val addPostViewModel    : AddPostViewModel by viewModels()
    lateinit var imageUri   : Uri
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner  = this
        binding.addPostModel    = addPostViewModel

        addPostViewModel.userProfile( requireActivity() , binding.viewImageAddPostProfileId , binding.textViewNameProfileAddPostId )

        // add post
        binding.progressBarAddPostId.visibility = View.INVISIBLE
        binding.buttAddPostId.setOnClickListener {
            try{
                addPostViewModel.addPost( requireActivity() , view, imageUri , binding.editAddPostId , binding.progressBarAddPostId)
            }catch (e:Exception){
                Toast.makeText(requireActivity() , "Please select image",Toast.LENGTH_SHORT).show()
            }
        }

        // butt go show post page
        binding.buttGoShowPostPageId.setOnClickListener {
            binding.progressBarAddPostId.visibility = View.VISIBLE

            findNavController().navigate(R.id.action_addPostFragment_to_showPostFragment)

            binding.progressBarAddPostId.visibility = View.INVISIBLE
        }


        // text view butt log out
        binding.texViewGoProfileId.setOnClickListener {
            findNavController().navigate(R.id.action_addPostFragment_to_profileFragment)
        }

        // text view butt logout
        binding.textViewLogoutId.setOnClickListener {
            var alert = AlertDialog.Builder(requireActivity())
            alert.setTitle("are you need logout")
            alert.setMessage("click yes will go login page")
            alert.setPositiveButton("yes"){dialog,which->

                addPostViewModel.userLogout( view)
            }
            alert.setNegativeButton("no",null)
            alert.create().show()
        }
        // select image
        binding.addPostImageId.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){
            imageUri = data?.data!!
            binding.addPostImageId.setImageURI(imageUri)
        }
    }
}