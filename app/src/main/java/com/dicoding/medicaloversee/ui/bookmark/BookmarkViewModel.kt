package com.dicoding.medicaloversee.ui.bookmark

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.medicaloversee.data.local.entity.HospitalEntity
import com.dicoding.medicaloversee.data.remote.HospitalRepository
import com.dicoding.medicaloversee.di.Injection

class BookmarkViewModel(private val userRepository: HospitalRepository) : ViewModel() {

    fun getFavoriteHospitals() = userRepository.getAllFavorite()

    internal fun insertFavorite(favorite: HospitalEntity) {
        userRepository.insertFavorite(favorite)
    }

    internal fun deleteFavorite(favorite: HospitalEntity) {
        userRepository.deleteFavorite(favorite)
    }

    fun isFavoriteHospital(hospitalID: String): LiveData<Boolean> = userRepository.isFavoriteUser(hospitalID)
}

class BookmarkViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            val repository = Injection.provideRepository(context)
            @Suppress("UNCHECKED_CAST")
            return BookmarkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}