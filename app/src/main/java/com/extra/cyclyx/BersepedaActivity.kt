package com.extra.cyclyx

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.extra.cyclyx.ui.bersepeda.BersepedaFragment


class BersepedaActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bersepeda)
    }

    override fun onBackPressed() {
        val navHost = this.supportFragmentManager.findFragmentById(R.id.nav_host_bersepeda)
        val currFragment = navHost?.let {
            it.childFragmentManager.fragments[0]
        }
        Log.d("FRAGMENT","Current Fragment : $currFragment")
        if(currFragment is BersepedaFragment){
            Log.d("FRAGMENT","Current Fragment is a Bersepeda Fragment")
            showConfirmationDialog()
        }else{
            Log.d("FRAGMENT","Current Fragment is not Bersepeda Fragment")
            super.onBackPressed()
        }

    }


    private fun showConfirmationDialog(){
        AlertDialog.Builder(this)
            .setTitle("Anda Yakin Untuk Kembali Ke Home") // GPS not found
            .setMessage("Pada laman selanjutnya, bla bla bla") // Want to enable?
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                this.finish()
            })
            .setNegativeButton("TIDAK",DialogInterface.OnClickListener { _, _->

            })
            .show()
    }
}