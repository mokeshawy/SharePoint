package com.example.sharepoint.settingfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sharepoint.databinding.FragmentSettingBinding
import com.example.sharepoint.R

class SettingFragment : Fragment() {

    lateinit var binding    : FragmentSettingBinding
    val settingViewModel    : SettingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner  = this
        binding.settingModel    = settingViewModel

        // show user information
        settingViewModel.userProfile(requireActivity() , binding.viewImageSettingProfileId , binding.textViewNameUserSettingId)

        // go edit profile page
        binding.textViewEditProfileId.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_editProfileUserFragment)
        }

    }
}