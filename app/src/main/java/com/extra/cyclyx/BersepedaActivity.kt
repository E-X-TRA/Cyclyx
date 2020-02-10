package com.extra.cyclyx

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.extra.cyclyx.ui.bersepeda.BersepedaFragment
import com.extra.cyclyx.ui.selesaiSepeda.SelesaiBersepedaFragment


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
        if(currFragment is BersepedaFragment){
            showConfirmationDialog()
        }
        else if(currFragment is SelesaiBersepedaFragment){
            this.finish()
        }
        else{
            super.onBackPressed()
        }

    }


    private fun showConfirmationDialog(){
        AlertDialog.Builder(this)
            .setTitle("Anda Yakin Untuk Kembali Ke Home ?")
            .setMessage("Data yang anda lakukan akan ditampilkan dengan label error di riwayat bersepeda")
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                this.finish()
            })
            .setNegativeButton("TIDAK",DialogInterface.OnClickListener { _, _->

            })
            .show()
    }
}