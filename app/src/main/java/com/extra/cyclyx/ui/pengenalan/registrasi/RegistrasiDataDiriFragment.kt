package com.extra.cyclyx.ui.pengenalan.registrasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.extra.cyclyx.R
import com.extra.cyclyx.databinding.FragmentRegistrasiDataDiriBinding
import com.extra.cyclyx.entity.User

/**
 * A simple [Fragment] subclass.
 */
class RegistrasiDataDiriFragment : Fragment() {
    private lateinit var binding : FragmentRegistrasiDataDiriBinding
    private lateinit var viewModel : RegistrasiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrasiDataDiriBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val app = activity?.application
        viewModel = ViewModelProviders.of(this,RegistrasiViewModel.Factory(app!!)).get(RegistrasiViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val userModel = User()
            userModel.firstName = binding.edtNamaDepan.text.toString()
            userModel.lastName = binding.edtNamaBelakang.text.toString()
            userModel.birthYear = Integer.parseInt(binding.edtThnLahir.text.toString())
            userModel.weight = Integer.parseInt(binding.edtBerat.text.toString())
            userModel.height = Integer.parseInt(binding.edtTinggi.text.toString())

            viewModel.onSaveUserData(userModel)
            this.moveToNextRegistration()
        }
    }

    private fun moveToNextRegistration(){
        val registrasiGenderFragment = RegistrasiGenderFragment()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_content, registrasiGenderFragment)
            ?.addToBackStack(null)
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.commit()
    }

}

