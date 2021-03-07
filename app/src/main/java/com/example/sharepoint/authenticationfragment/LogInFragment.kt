package com.example.sharepoint.authenticationfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sharepoint.R
import com.example.sharepoint.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    lateinit var binding    : FragmentLogInBinding
    val viewModelLogIn      : LogInViewModel by viewModels()
    lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with logIn viewModel
        binding.lifecycleOwner  = this
        binding.logInModel      = viewModelLogIn


        // log in with email and password
        binding.progressBarLogInId.visibility = View.INVISIBLE
        binding.buttLogIn.setOnClickListener {
            viewModelLogIn.logIn(requireActivity() , view , binding.editEmailLogInId , binding.editPassLogInId , binding.progressBarLogInId)
        }

        // go create account
        binding.textGoCreateAccountId.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_createAccountFragment)
        }
    }

}