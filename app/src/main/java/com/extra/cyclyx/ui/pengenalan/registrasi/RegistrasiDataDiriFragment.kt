package com.extra.cyclyx.ui.pengenalan.registrasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.extra.cyclyx.R
import com.extra.cyclyx.databinding.FragmentRegistrasiDataDiriBinding
import com.extra.cyclyx.entity.User
import com.extra.cyclyx.utils.WARNING_TYPES

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
            userModel.birthYear = binding.edtThnLahir.text.toString()
            userModel.weight = binding.edtBerat.text.toString()
            userModel.height = binding.edtTinggi.text.toString()

            viewModel.onSaveUserData(userModel)
        }

        binding.btnBack.setOnClickListener {
            moveToPreviousFragment()
        }

        viewModel.navigateNext.observe(this, Observer {
            it?.let {
                if (it) {
                    this.moveToNextRegistration()
                }
            }
        })

        viewModel.showWarning.observe(this, Observer {
            it?.let{
                when(it){
                    WARNING_TYPES.REGISTRATION_MUST_NOT_NULL -> {
                        Toast.makeText(context,"Anda Harus Mengisi Semua Data!", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(context,"Terjadi Sebuah Kesalahan", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun moveToNextRegistration(){
        val registrasiGenderFragment = RegistrasiGenderFragment()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_content, registrasiGenderFragment)
            ?.addToBackStack(null)
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.commit()
    }

    private fun moveToPreviousFragment(){
        activity?.onBackPressed()
    }

}

