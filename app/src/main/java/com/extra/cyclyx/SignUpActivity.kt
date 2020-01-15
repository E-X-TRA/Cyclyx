package com.extra.cyclyx

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.extra.cyclyx.utils.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        val sharedPreferences = getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)

        //save data in shared preferences

        btn_signUp.setOnClickListener {
            val email = edt_email.text.toString().trim()
            val username = edt_username.text.toString().trim()
            val password = edt_password.text.toString().trim()
            val confirmPassword = edt_confirm_password.text.toString().trim()

            val editor = sharedPreferences.edit()

            editor.putString(USER_EMAIL,email)
            editor.putString(USER_USERNAME, username)
            editor.putString(USER_PASSWORD, password)
            editor.putString(USER_TOKEN,RandomStringGenerator().getRandomString(32))

            editor.apply()
        }

        showdata.setOnClickListener {
            val mail = sharedPreferences.getString(USER_EMAIL, "")
            val uname = sharedPreferences.getString(USER_USERNAME, "")
            val ps = sharedPreferences.getString(USER_PASSWORD, "")

            Log.d("TRACKING","Email: $mail \nUsername: $uname \nPassword: $ps")
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
}
