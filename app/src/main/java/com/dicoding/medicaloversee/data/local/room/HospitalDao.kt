package com.dicoding.medicaloversee.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.medicaloversee.data.local.entity.HospitalEntity

@Dao
interface HospitalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: HospitalEntity)

    @Delete
    fun deleteFavorite(favorite: HospitalEntity)

    @Query("SELECT * FROM hospital where favorited = 1")
    fun getAllFavorites(): LiveData<List<HospitalEntity>>

    @Query("SELECT EXISTS(SELECT * FROM hospital WHERE hospitalID = :hospitalID AND favorited = 1)")
    fun isFavoriteHospital(hospitalID: String): LiveData<Boolean>
}