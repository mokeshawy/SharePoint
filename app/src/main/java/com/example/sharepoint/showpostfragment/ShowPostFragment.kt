package com.example.sharepoint.showpostfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
        showPostViewModel.showUserProfile(requireActivity() , binding.viewImageShowPostProfileId , binding.textViewNameProfileShowPostId)

        showPostViewModel.showPost()
        showPostViewModel.showPostForAllUser.observe(viewLifecycleOwner, Observer {

            binding.recyclerShowPostId.adapter = RecyclerShowPostAdapter(it)
        })

    }
}