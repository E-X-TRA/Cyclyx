package com.extra.cyclyx.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.extra.cyclyx.Entity.Bersepeda
import com.extra.cyclyx.Entity.Pengguna

@Database(entities = [Bersepeda::class,Pengguna::class],version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun sepedahanDAO() : BersepedaDao
    abstract fun userDAO() : PenggunaDao

    companion object{
        var INSTANCE : AppDatabase? = null

        fun getAppDataBase(context: Context) : AppDatabase?{
            if(INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,"cyclyxDB"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    fun destroyDataBase(){
        INSTANCE = null
    }
}