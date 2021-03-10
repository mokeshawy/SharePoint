package com.example.sharepoint.updateemailfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentUpdateEmailBinding


class UpdateEmailFragment : Fragment() {

    lateinit var binding        : FragmentUpdateEmailBinding
    val updateEmailViewModel    : UpdateEmailViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateEmailBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner      = this
        binding.updateEmailModel    = updateEmailViewModel

        binding.progressBarUpdateEmailId.visibility = View.INVISIBLE
        binding.buttUpdateEmailId.setOnClickListener {
            updateEmailViewModel.updateEmail(requireActivity() , view , binding.editTextUpdateEmailId , binding.progressBarUpdateEmailId)
        }
    }
}