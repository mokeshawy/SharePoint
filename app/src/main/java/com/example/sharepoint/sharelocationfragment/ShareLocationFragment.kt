package com.example.sharepoint.sharelocationfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentShareLocationBinding

class ShareLocationFragment : Fragment() {

    lateinit var binding        : FragmentShareLocationBinding
    val shareLocationViewModel  : ShareLocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentShareLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connection with share view model
        binding.lifecycleOwner      = this
        binding.shareLocationModel  = shareLocationViewModel

        // view information for user log in
        shareLocationViewModel.readData(requireActivity() , binding.textViewNameUserLogInId , binding.viewImageHomeProfileId)

        // butt send location for user log in
        binding.buttSendMyLocationId.setOnClickListener {
            shareLocationViewModel.senYourLocation(requireContext())
        }
    }
}