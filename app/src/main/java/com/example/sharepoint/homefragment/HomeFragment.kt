package com.example.sharepoint.homefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding    : FragmentHomeBinding
    val homeViewModel       : HomeViewModel by viewModels()



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner  = this
        binding.modelHome       = homeViewModel

        // call for show data for user profile from dataStore
        homeViewModel.readData(requireActivity(), binding.textViewNameUserLogInId , binding.viewImageHomeProfileId)

        homeViewModel.showUser()
        homeViewModel.allUserSignUpShow.observe(viewLifecycleOwner, Observer {

            binding.recyclerShowUserId.adapter = RecyclerHomeFragmentAdapter(it,requireActivity())
        })
    }
}