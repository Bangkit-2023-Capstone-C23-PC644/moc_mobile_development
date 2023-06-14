package com.dicoding.medicaloversee.di

import android.content.Context
import com.dicoding.medicaloversee.data.local.room.FavoriteDatabase
import com.dicoding.medicaloversee.data.remote.HospitalRepository
import com.dicoding.medicaloversee.data.remote.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context): HospitalRepository {
        val apiService = ApiConfig().getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.hospitalDao()
        return HospitalRepository.getInstance(apiService, dao)
    }
}