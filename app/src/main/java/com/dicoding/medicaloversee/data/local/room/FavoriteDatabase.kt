package com.dicoding.medicaloversee.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.medicaloversee.data.local.entity.HospitalEntity

@Database(
    entities = [HospitalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase: RoomDatabase() {

    abstract fun hospitalDao(): HospitalDao

    companion object {
        @Volatile
        private var instance: FavoriteDatabase? = null
        fun getInstance(context: Context): FavoriteDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java, "favorite.db"
                ).build()
            }
    }
}