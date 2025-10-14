package com.tomli.profftask.databases

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(entities = [UserData::class], version = 4,
    exportSchema = true, autoMigrations = [AutoMigration(from=1, to=2), AutoMigration(from=2, to=3),
        AutoMigration(from=3, to=4)])
abstract class ProffDB : RoomDatabase() {
    abstract val dao: Dao
    companion object{
        fun createDB(context: Context): ProffDB{
            return Room.databaseBuilder(context, ProffDB::class.java, "appdb.db")//.fallbackToDestructiveMigration()
                .createFromAsset("appdb.db").build()
        }
    }
}