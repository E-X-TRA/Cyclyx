package com.extra.cyclyx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Toast.makeText(this,applicationContext.resources.getString(R.string.mapbox_token),Toast.LENGTH_LONG).show()
        //da navview
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        //da navcontroller
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // da appbar (unused really)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_riwayat, R.id.navigation_profil
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        //implmenenting da navview
        navView.setupWithNavController(navController)
    }
}
