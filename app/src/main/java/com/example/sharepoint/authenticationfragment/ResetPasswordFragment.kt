package com.example.sharepoint.authenticationfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {

    lateinit var binding    : FragmentResetPasswordBinding
     val viewModelResetPass : ResetPasswordViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.resetPassModel = viewModelResetPass


        //reset password
        binding.progressBarResetPassId.visibility = View.INVISIBLE
        binding.buttResetPassId.setOnClickListener {
            viewModelResetPass.resetPassword(requireActivity() , view , binding.editTextResetPassId , binding.progressBarResetPassId)
        }

    }
}