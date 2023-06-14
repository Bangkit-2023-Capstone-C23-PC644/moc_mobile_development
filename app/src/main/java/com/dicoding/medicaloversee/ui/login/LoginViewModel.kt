package com.dicoding.medicaloversee.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.medicaloversee.data.remote.response.LoginRequest
import com.dicoding.medicaloversee.data.remote.response.LoginResponse
import com.dicoding.medicaloversee.data.remote.retrofit.ApiConfig
import com.dicoding.medicaloversee.model.UserModel
import com.dicoding.medicaloversee.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isSuccess = MutableLiveData<String>()
    val isSuccess: LiveData<String> = _isSuccess

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun loginUser(nik: String, password: String) {
        _isLoading.value = true
        if (!isInputValid(nik, password)) {
            _isError.value = true
            _isLoading.value = false
            return
        }

        val apiService = ApiConfig().getApiService()
        val request = LoginRequest(nik, password)
        val loginUserRequest = apiService.loginUser(request)
        loginUserRequest.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        saveUser(UserModel(nik, responseBody.loginResult.name, responseBody.loginResult.email, responseBody.loginResult.phone, password, responseBody.loginResult.lintang, responseBody.loginResult.bujur, true, responseBody.loginResult.token))
                        _isSuccess.value = responseBody.message
                    }else {
                        _isError.value = true
                    }
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    private fun isInputValid(nik: String, password: String): Boolean {
        if (nik.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }
}