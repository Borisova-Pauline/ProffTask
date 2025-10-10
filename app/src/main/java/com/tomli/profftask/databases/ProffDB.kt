package com.tomli.profftask.databases

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(entities = [UserData::class], version = 1,
    exportSchema = true)
abstract class ProffDB : RoomDatabase() {
    abstract val dao: Dao
    companion object{
        fun createDB(context: Context): ProffDB{
            return Room.databaseBuilder(context, ProffDB::class.java, "appdb.db")//.fallbackToDestructiveMigration()
                .createFromAsset("appdb.db").build()
        }
    }
}