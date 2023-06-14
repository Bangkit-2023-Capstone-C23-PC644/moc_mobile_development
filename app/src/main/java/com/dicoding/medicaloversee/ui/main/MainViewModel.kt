package com.dicoding.medicaloversee.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.medicaloversee.model.UserModel
import com.dicoding.medicaloversee.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isSuccess = MutableLiveData<String>()
    val isSuccess: LiveData<String> = _isSuccess

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    /*fun updateLocation(lintang: String, bujur: String) {
        viewModelScope.launch {
            pref.updateLocation(lintang, bujur)
        }
    }*/
}