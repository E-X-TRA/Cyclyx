package com.extra.cyclyx

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val mWaitHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mWaitHandler.postDelayed({
            try {
                intent = determineNextPage()
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }, 2600)  // Delay dalam millisekon
        mWaitHandler.postDelayed({
            finish()
        },4000)
    }

    private fun determineNextPage() : Intent{
        val sharedPreferences = getSharedPreferences("CYCLYX_PROFILE", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("USER_TOKEN","")
        Log.d("TOKEN","User Token => '$token'")
        if(!token.equals("")){
            return Intent(applicationContext,MainActivity::class.java)
        }else{
            return Intent(applicationContext,SignUpActivity::class.java)
        }
    }
}
