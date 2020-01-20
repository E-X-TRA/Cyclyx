package com.extra.cyclyx

import GambaranFiturFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.extra.cyclyx.ui.pengenalan.registrasi.RegistrasiDataDiriFragment
import com.extra.cyclyx.utils.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*
import kotlin.collections.ArrayList

class SignUpActivity : AppCompatActivity(){

    lateinit var screenPage: ViewPager
    var introViewPagerAdapter:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        setContentView(R.layout.activity_sign_up)
    }

            editor.apply()

            val intent =  Intent(this, RegistrasiDataDiriFragment::class.java)
            startActivity(intent)
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


}
