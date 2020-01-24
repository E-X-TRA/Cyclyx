package com.extra.cyclyx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.extra.cyclyx.ui.pengenalan.gambaran.GambaranCoverFragment
import java.util.*


class SignUpActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val fragmentManager: FragmentManager = supportFragmentManager
//        supportFragmentManager inisialisasi nilai FragmentManager untuk berinteraksi dengan Activity saat ini



        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//        Memulai transaction fragment manager

        val gambaranCoverFragment : GambaranCoverFragment = GambaranCoverFragment()
//        membuat objek fragment

        transaction.add(R.id.frame_content, gambaranCoverFragment)
//        menambahkan fragment

        transaction.addToBackStack("gambaranCoverFragment")
//        dapat menyimpan fragment ke dalam state ,ketika tombol back diklik

        transaction.commit()
//        mengeksekusi fragment transaction



    }


}



    class RandomStringGenerator{
        companion object {
            private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
        }

        fun getRandomString(sizeOfRandomString: Int): String {
            val random = Random()
            val sb = StringBuilder(sizeOfRandomString)
            for (i in 0 until sizeOfRandomString)
                sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
            return sb.toString()
        }
    }



