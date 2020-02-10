package com.extra.cyclyx.ui.pengenalan.registrasi


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.extra.cyclyx.MainActivity
import com.extra.cyclyx.R
import com.extra.cyclyx.databinding.FragmentRegistrasiGenderBinding
import com.extra.cyclyx.ui.pengenalan.PengenalanSelesaiFragment
import com.extra.cyclyx.utils.GENDER.FEMALE
import com.extra.cyclyx.utils.GENDER.MALE
import com.extra.cyclyx.utils.GENDER.UNSELECTED
import com.extra.cyclyx.utils.WARNING_TYPES

/**
 * A simple [Fragment] subclass.
 */
class RegistrasiGenderFragment : Fragment() {
    private lateinit var binding: FragmentRegistrasiGenderBinding
    private lateinit var viewModel: RegistrasiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrasiGenderBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val app = activity?.application
        viewModel = ViewModelProvider(this, RegistrasiViewModel.Factory(app!!))
            .get(RegistrasiViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gender.observe(viewLifecycleOwner, Observer { gender ->
            gender?.let {
                when (it) {
                    MALE -> {
                        binding.btnLelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan)
                        binding.btnPerempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                        binding.btnLelaki.setTextColor(Color.WHITE)
                        binding.btnPerempuan.setTextColor(resources.getColor(R.color.font_light_blue,activity?.theme))
                    }
                    FEMALE -> {
                        binding.btnPerempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan)
                        binding.btnLelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                        binding.btnPerempuan.setTextColor(Color.WHITE)
                        binding.btnLelaki.setTextColor(resources.getColor(R.color.font_light_blue,activity?.theme))
                    }
                    UNSELECTED -> {
                        binding.btnLelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                        binding.btnPerempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                        binding.btnLelaki.setTextColor(resources.getColor(R.color.font_light_blue,activity?.theme))
                        binding.btnPerempuan.setTextColor(resources.getColor(R.color.font_light_blue,activity?.theme))
                    }
                }
            }
        })

        binding.btnPerempuan.setOnClickListener {
            viewModel.onClickGenderButton(FEMALE)
        }

        binding.btnLelaki.setOnClickListener {
            viewModel.onClickGenderButton(MALE)
        }

        binding.btnNext.setOnClickListener {
            viewModel.onSaveGenderUserData()
        }

        binding.btnBack.setOnClickListener {
            this.moveToPreviousFragment()
        }

        viewModel.showWarning.observe(viewLifecycleOwner, Observer {
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

        viewModel.navigateNext.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }

    private fun moveToLastFragment() {
        val gambaranSelesaiFragment =
            PengenalanSelesaiFragment()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_content, gambaranSelesaiFragment)
            ?.addToBackStack(null)
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.commit()
    }

    private fun moveToPreviousFragment(){
        activity?.onBackPressed()
    }


}
