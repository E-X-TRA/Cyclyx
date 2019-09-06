package com.extra.cyclyx.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.entity.Pengguna

@Database(entities = [Bersepeda::class,Pengguna::class],version = 1,exportSchema = false)
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