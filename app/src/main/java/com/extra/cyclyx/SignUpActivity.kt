package com.extra.cyclyx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.extra.cyclyx.ui.pengenalan.PengenalanFragment


class SignUpActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val fragmentManager: FragmentManager = supportFragmentManager
//        supportFragmentManager inisialisasi nilai FragmentManager untuk berinteraksi dengan Activity saat ini



        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//        Memulai transaction fragment manager

        val pengenalanFragment : PengenalanFragment =
            PengenalanFragment()
//        membuat objek fragment

        transaction.add(R.id.frame_content, pengenalanFragment)
//        menambahkan fragment

        transaction.addToBackStack("gambaranCoverFragment")
//        dapat menyimpan fragment ke dalam state ,ketika tombol back diklik

        transaction.commit()
//        mengeksekusi fragment transaction



    }


}







