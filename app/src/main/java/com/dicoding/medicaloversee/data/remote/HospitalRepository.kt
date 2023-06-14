package com.dicoding.medicaloversee.data.remote

import androidx.lifecycle.LiveData
import com.dicoding.medicaloversee.data.local.entity.HospitalEntity
import com.dicoding.medicaloversee.data.local.room.HospitalDao
import com.dicoding.medicaloversee.data.remote.retrofit.ApiService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HospitalRepository private constructor(
    private val apiService: ApiService,
    private val hospitalDao: HospitalDao
) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun getAllFavorite(): LiveData<List<HospitalEntity>> = hospitalDao.getAllFavorites()

    fun insertFavorite(favorite: HospitalEntity) {
        executorService.execute { hospitalDao.insertFavorite(favorite) }
    }

    fun deleteFavorite(userEntity: HospitalEntity) {
        executorService.execute {hospitalDao.deleteFavorite(userEntity) }
    }

    fun isFavoriteUser(hospitalID: String): LiveData<Boolean> = hospitalDao.isFavoriteHospital(hospitalID)

    companion object {
        @Volatile
        private var instance: HospitalRepository? = null
        fun getInstance(
            apiService: ApiService,
            hospitalDao: HospitalDao
        ): HospitalRepository =
            instance ?: synchronized(this) {
                instance ?: HospitalRepository(apiService, hospitalDao)
            }.also { instance = it }
    }
}