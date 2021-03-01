package com.example.sharepoint.authenticationfragment

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
import androidx.navigation.fragment.findNavController
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment() {

    lateinit var binding        : FragmentCreateAccountBinding
    val createAccountViewModel  : CreateAccountViewModel by viewModels()
    lateinit var imageUri       : Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // connect with create account view model
        binding.lifecycleOwner     = this
        binding.createAccountModel = createAccountViewModel


        // butt create account
        binding.buttCreateAccountId.setOnClickListener {
            try {
                createAccountViewModel.createAccount(requireActivity(),
                        view,
                        imageUri ,
                        binding.editNameCreateAccountId ,
                        binding.editPassCreateAccountId ,
                        binding.editEmailCreateAccountId ,
                        binding.editPhoneCreateAccountId)
            }catch (e:Exception){
                Toast.makeText(requireActivity(),"Please select image",Toast.LENGTH_SHORT).show()
            }
        }


        // select profile image for create new account
        binding.viewImageCreateAccountId.setOnClickListener {

            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

    }

    // fun onActivityResult for Action Pick photo by request code
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK){
            imageUri = data?.data!!
            binding.viewImageCreateAccountId.setImageURI(imageUri)
        }
    }
}