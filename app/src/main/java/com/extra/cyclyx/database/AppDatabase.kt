package com.extra.cyclyx.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.extra.cyclyx.database.converter.LocationConverter
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.entity.Tantangan

@Database(entities = [Bersepeda::class, Tantangan::class],version = 2,exportSchema = false)
@TypeConverters(LocationConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract val bersepedaDAO : BersepedaDao
    abstract val tantanganDAO : TantanganDao

    companion object{
        var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "CyclyxDB"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}