package com.extra.cyclyx

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.extra.cyclyx.repository.CyclyxRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.junit.After
import org.junit.Before
import org.junit.Test

class TestingFirebase {
    lateinit var context: Context
    lateinit var repository : CyclyxRepository
    @Before
    fun setup(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun mainTest(){
        repository = CyclyxRepository(context)
    }

    @After
    fun getRepoData(){
        val myRef = repository.firebaseDB.getReference("message")
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val value = p0.getValue(String::class.java)
                Log.d("FIREBASE","Value is: $value")
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d("FIREBASE","Failed to get data")
            }

        })
//        Log.d("FIREBASE","$myRef")
//        myRef.setValue("Hello World!").addOnSuccessListener {
//            Log.d("FIREBASE","Success!")
//        }.addOnFailureListener{
//            Log.d("FIREBASE","Failed!")
//        }

    }
}