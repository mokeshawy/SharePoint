package com.example.sharepoint.showpostfragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentShowPostBinding

class ShowPostFragment : Fragment() {

    lateinit var binding    : FragmentShowPostBinding
    val showPostViewModel   : ShowPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowPostBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner  = this
        binding.showPostModel   = showPostViewModel

        // show user profile
        showPostViewModel.userProfile(requireActivity() , binding.viewImageShowPostProfileId , binding.textViewNameProfileShowPostId)

        showPostViewModel.showPost()
        showPostViewModel.showPostForAllUser.observe(viewLifecycleOwner, Observer {

            binding.recyclerShowPostId.adapter = RecyclerShowPostAdapter(it)
        })



        // text view butt log out
        binding.texViewGoProfileId.setOnClickListener {
            findNavController().navigate(R.id.action_showPostFragment_to_profileFragment)
        }

        // text view butt logout
        binding.textViewLogoutId.setOnClickListener {
            var alert = AlertDialog.Builder(requireActivity())
            alert.setTitle("are you need logout")
            alert.setMessage("click yes will go login page")
            alert.setPositiveButton("yes"){dialog,which->

                showPostViewModel.userLogout( view)
            }
            alert.setNegativeButton("no",null)
            alert.create().show()
        }
    }
}